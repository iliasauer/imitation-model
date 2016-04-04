package ru.ifmo.kot.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.QueueSystem;
import ru.ifmo.kot.queue.system.storage.StorageFactory;
import ru.ifmo.kot.queue.system.storage.Discipline;

public class CliRunner implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(CliRunner.class);

    private static boolean hasBeenStarted = false;

    private final int jobs;
    private final int workers;
    private final int storage;
    private final Discipline discipline;
    private final int interval;
    private final int process;
    private final int runs;

    public CliRunner(int jobs, int workers,
                     int storage, Discipline discipline,
                     int interval, int process,
                     int runs) {
        this.jobs = jobs;
        this.workers = workers;
        this.storage = storage;
        this.discipline = discipline;
        this.interval = interval;
        this.process = process;
        this.runs = runs;
    }

    public static void start(String ... args) {
        final int jobs, workers,
                storage, interval,
                process, runs;
        final Discipline discipline;
        if (args.length != 0) {
            jobs = Integer.parseInt(args[0]);
            workers = Integer.parseInt(args[1]);
            storage = Integer.parseInt(args[2]);
            interval = Integer.parseInt(args[4]);
            process = Integer.parseInt(args[5]);
            if (args.length == 6) {
                runs = 1;
            } else {
                runs = Integer.parseInt(args[6]);
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
        }
        if (!hasBeenStarted) {
            new CliRunner(jobs, workers,
                    storage, discipline,
                    interval, process,
                    runs).run();
            hasBeenStarted = true;
        } else {
            LOGGER.error("The command line mode has already started");
        }
    }


    @Override
    public void run() {
        for (int i = 0; i < runs; i++) {
            LOGGER.info("Run #" + (i + 1));
            QueueSystem.run(jobs, workers,
                    storage, discipline,
                    interval, process);
            QueueSystem.shutdown();
        }
    }
}
