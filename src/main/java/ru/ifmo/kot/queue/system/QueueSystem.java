package ru.ifmo.kot.queue.system;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.engine.Engine;
import ru.ifmo.kot.queue.system.engine.Worker;
import ru.ifmo.kot.queue.system.job.Job;
import ru.ifmo.kot.queue.system.storage.Discipline;
import ru.ifmo.kot.queue.system.storage.StorageFactory;
import ru.ifmo.kot.queue.util.chart.Chart;
import ru.ifmo.kot.queue.util.chart.Charts;
import ru.ifmo.kot.queue.util.chart.Point;
import ru.ifmo.kot.queue.util.random.ComplexRandom;
import ru.ifmo.kot.queue.util.random.RandomGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.*;

import static ru.ifmo.kot.queue.system.OutputCharts.*;
import static ru.ifmo.kot.queue.util.random.ComplexRandom.exponentialDistributionDensityAtPoint;
import static ru.ifmo.kot.queue.util.random.ComplexRandom.exponentialDistributionFunctionAtPoint;

public class QueueSystem {

    private static final FastDateFormat TIME_FORMAT = FastDateFormat.getInstance("HH:mm:ss.SSS",
            TimeZone.getTimeZone("GMT"), Locale.getDefault());
    private static final Logger LOGGER = LogManager.getLogger(QueueSystem.class);

    public static final Map<String, String> RUN_PARAM_NAMES = new HashMap<>();
    public static final Map<String, String> OUTPUT_PARAM_NAMES = new HashMap<>();
    public static final Map<String, List<Map<String, String>>> IO_MAP = new HashMap<>();
    public static final List<Chart> CHART_LIST = new ArrayList<>(4);

    static {
        RUN_PARAM_NAMES.put(InputData.NUMBER_OF_JOBS_KEY, "number of jobs");
        RUN_PARAM_NAMES.put(InputData.NUMBER_OF_WORKERS_KEY, "number of workers");
        RUN_PARAM_NAMES.put(InputData.STORAGE_CAPACITY_KEY, "capacity of storage");
        RUN_PARAM_NAMES.put(InputData.SERVICE_DISCIPLINE_KEY, "service discipline");
        RUN_PARAM_NAMES.put(InputData.INTERVAL_KEY, "average job entry interval");
        RUN_PARAM_NAMES.put(InputData.PROCESS_TIME_KEY, "average job processing time");
        OUTPUT_PARAM_NAMES.put(OutputData.SYSTEM_USE_FACTOR_KEY, "system use factor");
        OUTPUT_PARAM_NAMES.put(OutputData.AVG_JOB_QUEUE_TIME_KEY, "average job queue waiting time");
        OUTPUT_PARAM_NAMES.put(OutputData.AVG_JOB_SYSTEM_TIME_KEY, "average job system standing time");
        OUTPUT_PARAM_NAMES.put(OutputData.AVG_JOB_QUEUE_NUMBER_KEY, "average job queue number over time");
        OUTPUT_PARAM_NAMES.put(OutputData.AVG_JOB_SYSTEM_NUMBER_KEY, "average job system number over time");
        OUTPUT_PARAM_NAMES.put(OutputData.ABSOLUTE_SYSTEM_THROUGHPUT_KEY, "absolute system throughput");
        OUTPUT_PARAM_NAMES.put(OutputData.RELATIVE_SYSTEM_THROUGHPUT_KEY, "relative system throughput");
        IO_MAP.put(Statistics.INPUT_KEY, new ArrayList<Map<String, String>>());
        IO_MAP.put(Statistics.OUTPUT_KEY, new ArrayList<Map<String, String>>());
    }

    private static Engine engine = null;
    @SuppressWarnings("unused")
    private static int numberOfJobs, numberOfWorkers, capacityOfStorage,
            avgInterval, avgProcessTime;
    @SuppressWarnings("unused")
    private static String discipline;
    private static long startRunTime, finishRunTime, totalRunTime, successfullyCompletedJobs;
    private static double systemUseFactor, avgJobQueueTime, avgJobSystemTime,
            avgJobQueueNumber, avgJobSystemNumber, absoluteThroughput, relativeThroughput;
    private static int intervalSeed = 0;
    private static int processSeed = 0;
    private static int currentNumberOfJobs = 0;
    private static boolean isFirst = true;

    public static void run(int numberOfJobs, int numberOfWorkers,
                           int capacityOfStorage, Discipline discipline,
                           int avgInterval, int avgProcessTime,
                           int seed) {
        if (isFirst) {
            setSeed(seed);
        }
        engine = new Engine(numberOfWorkers, capacityOfStorage, discipline);
        RandomGenerator.Builder generatorBuilder = RandomGenerator.newBuilder();
        RandomGenerator intervalGenerator = getRandomGenerator(generatorBuilder,
                intervalSeed, avgInterval);
        RandomGenerator processGenerator = getRandomGenerator(generatorBuilder,
                processSeed, avgProcessTime);
        List<Integer> intervalGeneratorValues = new ArrayList<>();
        List<Integer> processGeneratorValues = new ArrayList<>();
        final List<Object> jobQueueNumbers = new ArrayList<>();
        final List<Object> jobSystemNumbers = new ArrayList<>();
        final List<Object> avgJobQueueNumbers = new ArrayList<>();
        final List<Object> avgJobSystemNumbers = new ArrayList<>();
        final List<Object> systemUseFactors = new ArrayList<>();
        saveInputParams(numberOfJobs, numberOfWorkers, capacityOfStorage,
                discipline, avgInterval, avgProcessTime);
        int counter = 0;
        LOGGER.debug("The system starts running.");
        final List<Future<?>> futures = new ArrayList<>();
        setStartRunTime();
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @SuppressWarnings("InfiniteLoopStatement")
            @Override
            public void run() {
                try {
                    final long curNumOfQJobs = currentNumberOfQueueJobs();
                    jobQueueNumbers.add(
                            new Point(currentTimeFromStart(), curNumOfQJobs));
                    final long curNumOfSysJobs = currentNumberOfJobs();
                    jobSystemNumbers.add(
                            new Point(currentTimeFromStart(), curNumOfSysJobs));
                    avgJobQueueNumbers.add(
                            new Point(currentTimeFromStart(),
                                    ((double) curNumOfQJobs) / QueueSystem.avgInterval));
                    avgJobSystemNumbers.add(
                            new Point(currentTimeFromStart(),
                                    ((double) curNumOfSysJobs) / QueueSystem.avgInterval));
                    systemUseFactors.add(
                            new Point(currentTimeFromStart(), currentSystemUseFactor()));
                } catch (NullPointerException ignored) {
                }
            }}, 100, 25, TimeUnit.MILLISECONDS);
        while (counter < numberOfJobs) {
            final Job nextJob =
                    new Job(generateJobComplexity(processGenerator, processGeneratorValues));
            final Future<?> jobFuture = engine.submit(nextJob);
            futures.add(jobFuture);
            try {
                TimeUnit.MILLISECONDS.sleep(
                        generateJobEntryInterval(intervalGenerator, intervalGeneratorValues));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentNumberOfJobs = counter;
            counter++;
        }
        awaitTasksCompletion(futures);
        setFinishRunTime();
        setTotalRunTime();
        setSuccessfullyCompletedJobs();
        LOGGER.info("The total run time: " + totalRunTimeString());
        saveCharts(intervalGeneratorValues, processGeneratorValues, jobQueueNumbers,
                jobSystemNumbers, avgJobQueueNumbers, avgJobSystemNumbers, systemUseFactors);
    }

    private static void setSeed(final int intervalSeed, final int processSeed) {
        QueueSystem.intervalSeed = intervalSeed;
        QueueSystem.processSeed = processSeed;
    }

    private static void setSeed(final int seed) {
        if (seed <= 0) {
            QueueSystem.setSeed(RandomGenerator.SEED_1, RandomGenerator.SEED_2);
        } else {
            QueueSystem.setSeed(seed, seed);
        }
    }

    private static RandomGenerator getRandomGenerator(RandomGenerator.Builder builder, int seed,
                                                      int goal) {
        builder.setSeed(seed);
        builder.setGoal(goal);
        return builder.build();
    }

    private static long currentNumberOfJobs() {
        return engine.getTaskCount();
    }

    private static long currentNumberOfQueueJobs() {
        return engine.getTaskCount() - engine.getCompletedTaskCount();
    }

    private static long currentTimeFromStart() {
        return System.currentTimeMillis() - startRunTime;
    }

    private static void saveInputParams(int numberOfJobs, int numberOfWorkers,
                                        int capacityOfStorage, Discipline discipline,
                                        int avgInterval, int avgProcessTime) {
        QueueSystem.numberOfJobs = numberOfJobs;
        QueueSystem.numberOfWorkers = numberOfWorkers;
        QueueSystem.capacityOfStorage = capacityOfStorage;
        QueueSystem.discipline = discipline.name();
        QueueSystem.avgInterval = avgInterval;
        QueueSystem.avgProcessTime = avgProcessTime;
        final Map<String, String> inputMap = new HashMap<>();
        inputMap.put(InputData.NUMBER_OF_JOBS_KEY, String.valueOf(numberOfJobs));
        inputMap.put(InputData.NUMBER_OF_WORKERS_KEY, String.valueOf(numberOfWorkers));
        inputMap.put(InputData.STORAGE_CAPACITY_KEY, String.valueOf(capacityOfStorage));
        inputMap.put(InputData.SERVICE_DISCIPLINE_KEY, discipline.name());
        inputMap.put(InputData.INTERVAL_KEY, String.valueOf(avgInterval));
        inputMap.put(InputData.PROCESS_TIME_KEY, String.valueOf(avgProcessTime));
        IO_MAP.get(Statistics.INPUT_KEY).add(inputMap);
    }

    private static void saveOutputParams() {
        final Map<String, String> outputMap = new HashMap<>();
        outputMap.put(OutputData.SYSTEM_USE_FACTOR_KEY, String.valueOf(QueueSystem.systemUseFactor));
        outputMap.put(OutputData.AVG_JOB_QUEUE_TIME_KEY, String.valueOf(QueueSystem.avgJobQueueTime));
        outputMap.put(OutputData.AVG_JOB_SYSTEM_TIME_KEY, String.valueOf(QueueSystem.avgJobSystemTime));
        outputMap.put(OutputData.AVG_JOB_QUEUE_NUMBER_KEY, String.valueOf(QueueSystem.avgJobQueueNumber));
        outputMap.put(OutputData.AVG_JOB_SYSTEM_NUMBER_KEY, String.valueOf(QueueSystem.avgJobSystemNumber));
        outputMap.put(OutputData.ABSOLUTE_SYSTEM_THROUGHPUT_KEY, String.valueOf(QueueSystem.absoluteThroughput));
        outputMap.put(OutputData.RELATIVE_SYSTEM_THROUGHPUT_KEY, String.valueOf(QueueSystem.relativeThroughput));
        IO_MAP.get(Statistics.OUTPUT_KEY).add(outputMap);
    }

    private static void saveCharts(final List<Integer> intervalGeneratorValues,
                                   final List<Integer> processGeneratorValues,
                                   final List<Object> jobQueueNumbers,
                                   final List<Object> jobSystemNumbers,
                                   final List<Object> avgJobQueueNumbers,
                                   final List<Object> avgJobSystemNumbers,
                                   final List<Object> systemUseFactors) {
        List<Double> minMaxIntervalList = ComplexRandom.fromMinToMaxList(intervalGeneratorValues);
        List<Double> minMaxProcessList = ComplexRandom.fromMinToMaxList(processGeneratorValues);
        List<Object> intervalValuesFunction = new ArrayList<>();
        List<Object> processValuesFunction = new ArrayList<>();
        List<Object> intervalValuesDensity = new ArrayList<>();
        List<Object> processValuesDensity = new ArrayList<>();
        for (Double value : minMaxIntervalList) {
            intervalValuesFunction.add(new Point(
                    value, exponentialDistributionFunctionAtPoint(value, avgInterval)));
        }
        for (Double value : minMaxProcessList) {
            processValuesFunction.add(
                    new Point(value, exponentialDistributionFunctionAtPoint(value,
                            avgProcessTime)));
        }
        for (Double value : minMaxIntervalList) {
            intervalValuesDensity.add(
                    new Point(value, exponentialDistributionDensityAtPoint(value, avgInterval)));
        }
        for (Double value : minMaxProcessList) {
            processValuesDensity.add(
                    new Point(value,exponentialDistributionDensityAtPoint(value, avgProcessTime)));
        }
        CHART_LIST.add(new Chart(INTERVAL_VALUES_FUNCTION_CHART_NAME, intervalValuesFunction));
        CHART_LIST.add(new Chart(PROCESS_VALUES_FUNCTION_CHART_NAME, processValuesFunction));
        CHART_LIST.add(new Chart(INTERVAL_VALUES_DENSITY_CHART_NAME, intervalValuesDensity));
        CHART_LIST.add(new Chart(PROCESS_VALUES_DENSITY_CHART_NAME, processValuesDensity));
        CHART_LIST.add(new Chart(JOB_QUEUE_NUMBERS_NAME, jobQueueNumbers));
        CHART_LIST.add(new Chart(JOB_SYSTEM_NUMBERS_NAME, jobSystemNumbers));
        CHART_LIST.add(new Chart(AVG_JOB_QUEUE_NUMBERS_NAME, avgJobQueueNumbers));
        CHART_LIST.add(new Chart(AVG_JOB_SYSTEM_NUMBERS_NAME, avgJobSystemNumbers));
        CHART_LIST.add(new Chart(SYSTEM_USE_FACTORS_NAME, systemUseFactors));
    }

    private static int generateJobComplexity(final RandomGenerator generator,
                                             final List<Integer> valuesList) {
        processSeed = generator.nextInt();
        valuesList.add(processSeed);
        return processSeed;
    }

    private static long generateJobEntryInterval(final RandomGenerator generator,
                                                 final List<Integer> valuesList) {
        intervalSeed = generator.nextInt();
        valuesList.add(processSeed);
        return intervalSeed;
    }

    private static void setStartRunTime() {
        startRunTime = System.currentTimeMillis();
    }

    private static void setFinishRunTime() {
        finishRunTime = System.currentTimeMillis();
    }

    private static void setTotalRunTime() {
        totalRunTime = finishRunTime - startRunTime;
    }

    private static void setSuccessfullyCompletedJobs() {
        successfullyCompletedJobs = engine.getCompletedTaskCount();
    }

    @SuppressWarnings("WeakerAccess")
    public static String totalRunTimeString() {
        return TIME_FORMAT.format(totalRunTime);
    }

    private static void awaitTasksCompletion(List<Future<?>> futures) {
        for (Future<?> each : futures) {
            try {
                each.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("The internal system error", e);
            }
        }
    }


    public static void shutdown() {
        LOGGER.debug("The system is shutting down.");
        engine.shutdown();
        collectStatistics();
        saveOutputParams();
        engine = null;
        Job.resetJobCounter();
        isFirst = false;
    }

    private static void collectStatistics() {
        calculateSystemUseFactor();
        calculateAvgJobQueueTime();
        calculateAvgJobSystemTime();
        calculateAvgJobQueueNumber();
        calculateAvgJobSystemNumber();
        calculateAbsoluteSystemThroughput();
        calculateRelativeSystemThroughput();
        Worker.STATISTICS.clear();
        Job.STATISTICS.clear();
    }

    private static double currentSystemUseFactor() {
        long workersUseTime = 0;
        for (Long workerUseTime : Worker.STATISTICS.values()) {
            workersUseTime += workerUseTime;
        }
        final double relativeWorkersUse = ((double) workersUseTime) / currentTimeFromStart();
        return relativeWorkersUse / QueueSystem.numberOfWorkers;
    }

    private static void calculateSystemUseFactor() {
        long workersUseTime = 0;
        for (Long workerUseTime : Worker.STATISTICS.values()) {
            workersUseTime += workerUseTime;
        }
        final double relativeWorkersUse = ((double) workersUseTime) / totalRunTime;
        QueueSystem.systemUseFactor =
                relativeWorkersUse / QueueSystem.numberOfWorkers;
    }

    private static double currentAvgJobQueueTime() {
        long jobQueueTimeSum = 0;
        for (Map<String, Long> jobTimes : Job.STATISTICS.values()) {
            jobQueueTimeSum += jobTimes.get(Job.IN_QUEUE_TIME);
        }
        return ((double) jobQueueTimeSum) / currentNumberOfQueueJobs();
    }

    private static void calculateAvgJobQueueTime() {
        long jobQueueTimeSum = 0;
        for (Map<String, Long> jobTimes : Job.STATISTICS.values()) {
            jobQueueTimeSum += jobTimes.get(Job.IN_QUEUE_TIME);
        }
        QueueSystem.avgJobQueueTime =
                ((double) jobQueueTimeSum) / QueueSystem.numberOfJobs;
    }

    private static double currentAvgJobSystemTime() {
        long jobSystemTimeSum = 0;
        for (Map<String, Long> jobTimes : Job.STATISTICS.values()) {
            jobSystemTimeSum += jobTimes.get(Job.IN_SYSTEM_TIME);
        }
        return ((double) jobSystemTimeSum) / currentNumberOfJobs();
    }

    private static void calculateAvgJobSystemTime() {
        long jobSystemTimeSum = 0;
        for (Map<String, Long> jobTimes : Job.STATISTICS.values()) {
            jobSystemTimeSum += jobTimes.get(Job.IN_SYSTEM_TIME);
        }
        QueueSystem.avgJobSystemTime =
                ((double) jobSystemTimeSum) / QueueSystem.numberOfJobs;
    }

//    private static double currentAvgJobQueueNumber() {
//        return currentAvgJobQueueTime() / QueueSystem.avgInterval;
//    }

    private static void calculateAvgJobQueueNumber() {
        QueueSystem.avgJobQueueNumber =
                QueueSystem.avgJobQueueTime / QueueSystem.avgInterval;
    }

//    private static double currentAvgJobSystemNumber() {
//        return currentAvgJobSystemTime() / QueueSystem.avgInterval;
//    }

    private static void calculateAvgJobSystemNumber() {
        QueueSystem.avgJobSystemNumber =
                QueueSystem.avgJobSystemTime / QueueSystem.avgInterval;
    }

    private static void calculateAbsoluteSystemThroughput() {
        QueueSystem.absoluteThroughput =
                ((double) successfullyCompletedJobs) / QueueSystem.totalRunTime;
    }

    private static void calculateRelativeSystemThroughput() {
        QueueSystem.relativeThroughput =
                ((double) successfullyCompletedJobs) / QueueSystem.numberOfJobs;
    }

}
