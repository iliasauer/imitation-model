package ru.ifmo.kot.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.InputData;
import ru.ifmo.kot.queue.system.OutputData;
import ru.ifmo.kot.queue.system.QueueSystem;
import ru.ifmo.kot.queue.system.Statistics;
import ru.ifmo.kot.queue.system.storage.StorageFactory;
import ru.ifmo.kot.queue.system.storage.Discipline;

import java.util.List;
import java.util.Map;

public class CliRunner implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(CliRunner.class);
    private static final StringBuilder startStopInfoBuilder = new StringBuilder();

    private static int runCounter = 0;
    private static boolean hasBeenStarted = false;

    private final int jobs;
    private final int workers;
    private final int storage;
    private final Discipline discipline;
    private final int interval;
    private final int process;
    private final int runs;
    private final int seed;

    public CliRunner(int jobs, int workers,
                     int storage, Discipline discipline,
                     int interval, int process,
                     int runs, int seed) {
        this.jobs = jobs;
        this.workers = workers;
        this.storage = storage;
        this.discipline = discipline;
        this.interval = interval;
        this.process = process;
        this.runs = runs;
        this.seed = seed;
    }

    public static void start(String ... args) throws IllegalArgumentException {
        final int jobs, workers,
                storage, interval,
                process, runs, seed;
        final Discipline discipline;
        if (args.length != 0) {
            jobs = Integer.parseInt(args[0]);
            workers = Integer.parseInt(args[1]);
            storage = Integer.parseInt(args[2]);
            interval = Integer.parseInt(args[4]);
            process = Integer.parseInt(args[5]);
            if (args.length >= 7) {
                runs = Integer.parseInt(args[6]);
            } else {
                runs = 1;
            }
            if (args.length == 8) {
                seed = Integer.parseInt(args[7]);
            } else {
                seed = 0;
            }
            discipline = StorageFactory.getDiscipline(args[3]);
            if (discipline == null) {
                throw new IllegalArgumentException();
            }
            checkParams(jobs, workers, storage, interval, process);
        } else {
            // the default configuration
            jobs = 100;
            workers = 6;
            storage = 10;
            interval = 180;
            process = 60;
            discipline = Discipline.LIFO;
            runs = 2;
            seed = 0;
        }
        if (!hasBeenStarted) {
            new CliRunner(jobs, workers,
                    storage, discipline,
                    interval, process,
                    runs, seed).run();
            hasBeenStarted = true;
        } else {
            LOGGER.error("The command line mode has already started");
        }
    }

    private static String getStartInfo(int jobs, int workers,
                                int storage, Discipline discipline, int interval,
                                int process, int runs, int generatorSeed) {
        startStopInfoBuilder.setLength(0);
        startStopInfoBuilder.append("The ")
                .append(QueueSystem.RUN_PARAM_NAMES.get(InputData.NUMBER_OF_JOBS_KEY))
                .append(": ").append(jobs).append("\n");
        startStopInfoBuilder.append("The ")
                .append(QueueSystem.RUN_PARAM_NAMES.get(InputData.NUMBER_OF_WORKERS_KEY))
                .append(": ").append(workers).append("\n");
        startStopInfoBuilder.append("The ")
                .append(QueueSystem.RUN_PARAM_NAMES.get(InputData.STORAGE_CAPACITY_KEY))
                .append(": ").append(storage).append("\n");
        startStopInfoBuilder.append("The ")
                .append(QueueSystem.RUN_PARAM_NAMES.get(InputData.SERVICE_DISCIPLINE_KEY))
                .append(": ").append(discipline.name()).append("\n");
        startStopInfoBuilder.append("The ")
                .append(QueueSystem.RUN_PARAM_NAMES.get(InputData.INTERVAL_KEY))
                .append(": ").append(interval).append("\n");
        startStopInfoBuilder.append("The ")
                .append(QueueSystem.RUN_PARAM_NAMES.get(InputData.PROCESS_TIME_KEY))
                .append(": ").append(process).append("\n");
        startStopInfoBuilder.append("The ")
                .append("number of runs: ").append(runs).append("\n");
        startStopInfoBuilder.append("The ")
                .append("generator seed: ");
        if (generatorSeed <= 0) {
            startStopInfoBuilder.append("not specified");
        } else {
            startStopInfoBuilder.append(generatorSeed);
        }
        startStopInfoBuilder.append("\n");
        return startStopInfoBuilder.toString();
    }

    private static String getStopInfo() {
        startStopInfoBuilder.setLength(0);
        List<Map<String, String>> outputList = QueueSystem.IO_MAP.get(Statistics.OUTPUT_KEY);
        Map<String, String> outputMap = outputList.get(outputList.size() - 1);
        String[] outputKeys = {OutputData.SYSTEM_USE_FACTOR_KEY, OutputData.AVG_JOB_QUEUE_TIME_KEY,
                OutputData.AVG_JOB_SYSTEM_TIME_KEY, OutputData.AVG_JOB_QUEUE_NUMBER_KEY,
                OutputData.AVG_JOB_SYSTEM_NUMBER_KEY, OutputData.ABSOLUTE_SYSTEM_THROUGHPUT_KEY,
                OutputData.RELATIVE_SYSTEM_THROUGHPUT_KEY};
        // full output
//        for (String outputKey: outputKeys) {
//            startStopInfoBuilder.append("The ")
//                    .append(QueueSystem.OUTPUT_PARAM_NAMES.get(outputKey))
//                    .append(": ").append(outputMap.get(outputKey)).append("\n");
//        }
        // csv-like output
        for (String outputKey: outputKeys) {
            startStopInfoBuilder.append(outputMap.get(outputKey)).append(", ");
        }
        final int builderLength = startStopInfoBuilder.length();
        startStopInfoBuilder.delete(builderLength - 2, builderLength);
        return startStopInfoBuilder.toString();
    }


    @Override
    public void run() {
        LOGGER.info(getStartInfo(jobs, workers,
                storage, discipline,
                interval, process,
                runs, seed));
        for (int i = 0; i < runs; i++) {
            runCounter++;
            LOGGER.info("Run #" + runCounter);
            QueueSystem.run(jobs, workers,
                    storage, discipline,
                    interval, process, seed);
            QueueSystem.shutdown();
            LOGGER.info(getStopInfo());
        }
    }

    private static void handleNotCorrectIntParam(int param, String name) throws
            IllegalArgumentException {
        if (param <= 0) {
            LOGGER.error("The parameter \"" + name + "\" is not correct.");
            throw new IllegalArgumentException();
        }
    }

    private static void checkParams(int jobs, int workers,
                                    int storage, int interval,
                                    int process) throws IllegalArgumentException {
        handleNotCorrectIntParam(jobs, QueueSystem.RUN_PARAM_NAMES.get(InputData.NUMBER_OF_JOBS_KEY));
        handleNotCorrectIntParam(workers, QueueSystem.RUN_PARAM_NAMES.get(InputData.NUMBER_OF_WORKERS_KEY));
        handleNotCorrectIntParam(storage, QueueSystem.RUN_PARAM_NAMES.get(InputData.STORAGE_CAPACITY_KEY));
        handleNotCorrectIntParam(interval, QueueSystem.RUN_PARAM_NAMES.get(InputData.INTERVAL_KEY));
        handleNotCorrectIntParam(process, QueueSystem.RUN_PARAM_NAMES.get(InputData.PROCESS_TIME_KEY));
    }
}
