package ru.ifmo.kot.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.QueueSystem;
import ru.ifmo.kot.queue.system.storage.StorageFactory;
import ru.ifmo.kot.queue.system.storage.Discipline;
import ru.ifmo.kot.queue.util.random.RandomGenerator;

public class CliRunner implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(CliRunner.class);
    private static final StringBuilder startInfoBuilder = new StringBuilder();

    private static boolean hasBeenStarted = false;

    private final int jobs;
    private final int workers;
    private final int storage;
    private final Discipline discipline;
    private final int interval;
    private final int process;
    private final int runs;
    private final int generatorSeed;

    public CliRunner(int jobs, int workers,
                     int storage, Discipline discipline,
                     int interval, int process,
                     int runs, int generatorSeed) {
        this.jobs = jobs;
        this.workers = workers;
        this.storage = storage;
        this.discipline = discipline;
        this.interval = interval;
        this.process = process;
        this.runs = runs;
        this.generatorSeed = generatorSeed;
    }

    public static void start(String ... args) {
        final int jobs, workers,
                storage, interval,
                process, runs, generatorSeed;
        final Discipline discipline;
        if (args.length != 0) {
            jobs = Integer.parseInt(args[0]);
            workers = Integer.parseInt(args[1]);
            storage = Integer.parseInt(args[2]);
            interval = Integer.parseInt(args[4]);
            process = Integer.parseInt(args[5]);
            if (args.length == 7) {
                runs = Integer.parseInt(args[6]);
            } else {
                runs = 1;
            }
            if (args.length == 8) {
                generatorSeed = Integer.parseInt(args[7]);
            } else {
                generatorSeed = RandomGenerator.SEED_1;
            }
            discipline = StorageFactory.getDiscipline(args[3]);
            if (discipline == null) {
                throw new IllegalArgumentException();
            }
        } else {
            // the default configuration
            jobs = 10;
            workers = 10;
            storage = 10;
            interval = 180;
            process = 60;
            discipline = Discipline.LIFO;
            runs = 2;
            generatorSeed = RandomGenerator.SEED_1;
        }
        if (!hasBeenStarted) {
            new CliRunner(jobs, workers,
                    storage, discipline,
                    interval, process,
                    runs, generatorSeed).run();
            hasBeenStarted = true;
        } else {
            LOGGER.error("The command line mode has already started");
        }
    }

    private String getStartInfo() {
        startInfoBuilder.setLength(0);
        startInfoBuilder.append("The number of jobs: ").append(jobs).append("\n");
        startInfoBuilder.append("The number of workers: ").append(workers).append("\n");
        startInfoBuilder.append("The storage capacity: ").append(storage).append("\n");
        startInfoBuilder.append("The service discipline: ").append(discipline.name()).append("\n");
        startInfoBuilder.append("The average job entry interval: ").append(interval).append("\n");
        startInfoBuilder.append("The average job processing time: ").append(process).append("\n");
        startInfoBuilder.append("The number of runs: ").append(runs).append("\n");
        startInfoBuilder.append("The generator seed: ").append(generatorSeed).append("\n");
        return startInfoBuilder.toString();
    }


    @Override
    public void run() {
        RandomGenerator.Builder generatorBuilder = RandomGenerator.newBuilder();
        generatorBuilder.setSeed(generatorSeed);
        LOGGER.info(getStartInfo());
        for (int i = 0; i < runs; i++) {
            LOGGER.info("Run #" + (i + 1));
            QueueSystem.run(jobs, workers,
                    storage, discipline,
                    interval, process, generatorBuilder);
            QueueSystem.shutdown();
        }
    }
}
