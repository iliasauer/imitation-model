package ru.ifmo.kot.queue.system;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.engine.Engine;
import ru.ifmo.kot.queue.system.job.Job;
import ru.ifmo.kot.queue.system.storage.Discipline;
import ru.ifmo.kot.queue.util.random.SimpleRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class QueueSystem {

    private static final Logger LOGGER = LogManager.getLogger(QueueSystem.class);

    private static Engine engine = null;

    public static void run(int numberOfJobs, int numberOfWorkers,
                           int capacityOfStorage, Discipline discipline,
                           int avgInterval, int avgProcessingTime) {
        engine = new Engine(numberOfWorkers, capacityOfStorage, discipline);
        int counter = 0;
        LOGGER.info("The system starts running.");
        avgProcessingTime = avgProcessingTime * 2 * 10;
        avgInterval = avgInterval * 2 * 10;
        List<Future<?>> futures = new ArrayList<>();
        while (counter < numberOfJobs) {
            futures.add(engine.submit(new Job(SimpleRandom.nextInt
                    (avgProcessingTime))));
            try {
                TimeUnit.MILLISECONDS.sleep((long) SimpleRandom.nextInt(avgInterval)); // todo TURN BACK!!!
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }
        awaitTasksCompletion(futures);
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
        Job.resetLogger();
    }
}
