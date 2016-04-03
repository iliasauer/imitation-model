package ru.ifmo.kot.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.QueueSystem;
import ru.ifmo.kot.queue.system.storage.StorageFactory.Discipline;

public class CommandLineRunner implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(CommandLineRunner.class);

    private static boolean hasBeenStarted = false;

    private final int numberOfJobs;
    private final int numberOfWorkers;
    private final int capacityOfStorage;
    private final Discipline discipline;
    private final int avgInterval;
    private final int avgProcessingTime;
    private final int numberOfRuns;

    private CommandLineRunner(int numberOfJobs, int numberOfWorkers,
                             int capacityOfStorage, Discipline discipline,
                             int avgInterval, int avgProcessingTime,
                             int numberOfRuns) {
        this.numberOfJobs = numberOfJobs;
        this.numberOfWorkers = numberOfWorkers;
        this.capacityOfStorage = capacityOfStorage;
        this.discipline = discipline;
        this.avgInterval = avgInterval;
        this.avgProcessingTime = avgProcessingTime;
        this.numberOfRuns = numberOfRuns;
    }

    public static void start(final int numberOfJobs, final int numberOfWorkers,
                             final int capacityOfStorage,
                             final Discipline discipline,
                             int avgInterval, int avgProcessingTime,
                             int numberOfRuns) {


        if (!hasBeenStarted) {
            new CommandLineRunner(numberOfJobs, numberOfWorkers,
                    capacityOfStorage, discipline,
                    avgInterval, avgProcessingTime,
                    numberOfRuns).run();;
            hasBeenStarted = true;
        } else {
            LOGGER.error("The command line mode has already started");
        }
    }

    public static void start(final int numberOfJobs, final int numberOfWorkers,
                             final int capacityOfStorage, final Discipline discipline,
                             int avgInterval, int avgProcessingTime) {
        start(numberOfJobs, numberOfWorkers,
                capacityOfStorage, discipline,
                avgInterval, avgProcessingTime,
                1);
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfRuns; i++) {
            LOGGER.info("Run #" + (i + 1));
            QueueSystem.run(numberOfJobs, numberOfWorkers,
                    capacityOfStorage, discipline,
                    avgInterval, avgProcessingTime);
        }
    }
}
