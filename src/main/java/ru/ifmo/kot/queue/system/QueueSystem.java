package ru.ifmo.kot.queue.system;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.engine.Engine;
import ru.ifmo.kot.queue.system.engine.Worker;
import ru.ifmo.kot.queue.system.job.Job;
import ru.ifmo.kot.queue.system.storage.Discipline;
import ru.ifmo.kot.queue.util.random.RandomGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class QueueSystem {

    @SuppressWarnings("WeakerAccess")
    public static final Map<String, Double> STATISTICS = new HashMap<>();
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("HH:mm:ss.SSS",
            TimeZone.getTimeZone("GMT")
            , Locale.getDefault());
    private static final Logger LOGGER = LogManager.getLogger(QueueSystem.class);

    private static Engine engine = null;
    private static long startRunTime;
    private static long finishRunTime;
    private static long totalRunTime;
    private static long successfullyCompletedJobs;
    private static int numberOfJobs;
    private static int numberOfWorkers;
    private static int avgInterval;

    public static void run(int numberOfJobs, int numberOfWorkers,
                           int capacityOfStorage, Discipline discipline,
                           int avgInterval, int avgProcessingTime,
                           RandomGenerator.Builder generatorBuilder) {
        engine = new Engine(numberOfWorkers, capacityOfStorage, discipline);
        generatorBuilder.setGoal(avgInterval);
        RandomGenerator intervalGenerator = generatorBuilder.build();
        generatorBuilder.setGoal(avgProcessingTime);
        RandomGenerator processGenerator = generatorBuilder.build();
        checkParams(numberOfJobs, numberOfWorkers, capacityOfStorage,
                avgInterval, avgProcessingTime);
        QueueSystem.numberOfJobs = numberOfJobs;
        QueueSystem.numberOfWorkers = numberOfWorkers;
        QueueSystem.avgInterval = avgInterval;
        int counter = 0;
        LOGGER.info("The system starts running.");
        final List<Future<?>> futures = new ArrayList<>();
        setStartRunTime();
        while (counter < numberOfJobs) {
            final Job nextJob = new Job(generateJobComplexity(processGenerator));
            final Future<?> jobFuture = engine.submit(nextJob);
            futures.add(jobFuture);
            try {
                TimeUnit.SECONDS.sleep(generateJobEntryInterval(intervalGenerator));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }
        awaitTasksCompletion(futures);
        setFinishRunTime();
        setTotalRunTime();
        setSuccessfullyCompletedJobs();
        LOGGER.info("The total run time: " + totalRunTimeString());
    }

    private static int generateJobComplexity(final RandomGenerator generator) {
        return generator.nextInt();
    }

    private static long generateJobEntryInterval(final RandomGenerator generator) {
        return generator.nextInt();
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
        return DATE_FORMAT.format(totalRunTime);
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

    private static void handleNotCorrectIntParam(int param, String name) {
        if (param <= 0) {
            LOGGER.error("The parameter \"" + name + "\" is not correct.");
        }
    }

    private static void checkParams(int numberOfJobs, int numberOfWorkers,
                                    int capacityOfStorage, int avgInterval,
                                    int avgProcessingTime) {
        handleNotCorrectIntParam(numberOfJobs, "The number of jobs");
        handleNotCorrectIntParam(numberOfWorkers, "The number of workers");
        handleNotCorrectIntParam(capacityOfStorage, "The capacity of the storage");
        handleNotCorrectIntParam(avgInterval, "The average job entry interval");
        handleNotCorrectIntParam(avgProcessingTime, "The average job processing time");
    }

    public static void shutdown() {
        LOGGER.info("The system is shutting down.");
        engine.shutdown();
        collectStatistics();
        engine = null;
        Job.resetJobCounter();
    }

    private static void collectStatistics() {
        calculateSystemUseFactor(numberOfWorkers);
        calculateAvgJobQueueTime(numberOfJobs);
        calculateAvgJobSystemTime(numberOfJobs);
        calculateAvgJobQueueNumber(avgInterval);
        calculateAvgJobSystemNumber(avgInterval);
        calculateAbsoluteSystemThroughput();
        calculateRelativeSystemThroughput(numberOfJobs);
    }

    private static void calculateSystemUseFactor(final int numberOfWorkers) {
        double sum = 0;
        for (Long each : Worker.STATISTICS.values()) {
            sum += (((double) each) / totalRunTime);
        }
        STATISTICS.put("systemUseFactor", ((sum / numberOfWorkers)) * 1000);
    }

    private static void calculateAvgJobQueueTime(final int numberOfJobs) {
        long sum = 0;
        for (Map<String, Long> each : Job.STATISTICS.values()) {
            sum += each.get(Job.IN_QUEUE_TIME);
        }
        STATISTICS.put("avgJobQueueTime", (((double) sum) / numberOfJobs) * 1000);
    }

    private static void calculateAvgJobSystemTime(final int numberOfJobs) {
        long sum = 0;
        for (Map<String, Long> each : Job.STATISTICS.values()) {
            sum += each.get(Job.IN_SYSTEM_TIME);
        }
        STATISTICS.put("avgJobSystemTime", (((double) sum) / numberOfJobs) * 1000);
    }

    private static void calculateAvgJobQueueNumber(final int avgInterval) {
        STATISTICS.put("avgJobQueueNumber", (
                STATISTICS.get("avgJobQueueTime") / (avgInterval * 1000)));
    }

    private static void calculateAvgJobSystemNumber(final int avgInterval) {
        STATISTICS.put("avgJobSystemNumber", (
                STATISTICS.get("avgJobSystemTime") / (avgInterval * 1000)));
        //TODO check it
    }

    private static void calculateAbsoluteSystemThroughput() {
        STATISTICS.put("absTp", ((double) successfullyCompletedJobs) / (totalRunTime * 1000));
    }

    private static void calculateRelativeSystemThroughput(final int numberOfJobs) {
        STATISTICS.put("relTp", ((double) successfullyCompletedJobs) / numberOfJobs);
    }
}
