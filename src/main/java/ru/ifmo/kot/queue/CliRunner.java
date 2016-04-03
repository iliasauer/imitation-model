package ru.ifmo.kot.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.QueueSystem;
import ru.ifmo.kot.queue.system.storage.StorageFactory;
import ru.ifmo.kot.queue.system.storage.StorageFactory.Discipline;

public class CliRunner implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(CliRunner.class);

    private static boolean hasBeenStarted = false;

    private final int numberOfJobs;
    private final int numberOfWorkers;
    private final int capacityOfStorage;
    private final Discipline discipline;
    private final int avgInterval;
    private final int avgProcessingTime;
    private final int numberOfRuns;

    private CliRunner(int numberOfJobs, int numberOfWorkers,
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

    public static void start(String ... args) {
        final int numberOfJobs, numberOfWorkers,
                capacityOfStorage, avgInterval,
                avgProcessingTime, numberOfRuns;
        final Discipline discipline;
        if (args.length != 0) {
            numberOfJobs = Integer.parseInt(args[0]);
            numberOfWorkers = Integer.parseInt(args[1]);
            capacityOfStorage = Integer.parseInt(args[2]);
            avgInterval = Integer.parseInt(args[4]);
            avgProcessingTime = Integer.parseInt(args[5]);
            if (args.length == 6) {
                numberOfRuns = 1;
            } else {
                numberOfRuns = Integer.parseInt(args[6]);
            }
            discipline = StorageFactory.getDiscipline(args[3]);
            if (discipline == null) {
                throw new IllegalArgumentException();
            }
        } else {
            // the default configuration
            numberOfJobs = 10;
            numberOfWorkers = 10;
            capacityOfStorage = 10;
            avgInterval = 180;
            avgProcessingTime = 60;
            discipline = Discipline.LIFO;
            numberOfRuns = 2;
        }
        if (!hasBeenStarted) {
            new CliRunner(numberOfJobs, numberOfWorkers,
                    capacityOfStorage, discipline,
                    avgInterval, avgProcessingTime,
                    numberOfRuns).run();;
            hasBeenStarted = true;
        } else {
            LOGGER.error("The command line mode has already started");
        }
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
