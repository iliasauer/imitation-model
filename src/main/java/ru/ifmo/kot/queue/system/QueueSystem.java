package ru.ifmo.kot.queue.system;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.engine.Engine;
import ru.ifmo.kot.queue.system.job.Job;
import ru.ifmo.kot.queue.system.storage.Discipline;
import ru.ifmo.kot.queue.util.random.RandomGenerator;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class QueueSystem {

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("HH:mm:ss.SSS",
            TimeZone.getTimeZone("GMT")
            , Locale.getDefault());
    private static final Logger LOGGER = LogManager.getLogger(QueueSystem.class);

    private static Engine engine = null;
    private static long startRunTime;
    private static long finishRunTime;

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
        int counter = 0;
        LOGGER.info("The system starts running.");
        List<Future<?>> futures = new ArrayList<>();
        setStartRunTime();
        while (counter < numberOfJobs) {
            futures.add(engine.submit(new Job(generateJobComplexity(processGenerator))));
            try {
                TimeUnit.SECONDS.sleep(generateJobEntryInterval(intervalGenerator));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }
        awaitTasksCompletion(futures);
        setFinishRunTime();
        LOGGER.info("The total run time: " + totalRunTime());
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

    public static String totalRunTime() {
        final long delta = finishRunTime - startRunTime;
        return DATE_FORMAT.format(finishRunTime - startRunTime);
    }

    private static void awaitTasksCompletion(List<Future<?>> futures) {
        for (Future<?> each: futures) {
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
        engine = null;
        Job.resetJobCounter();
    }
}
