package ru.ifmo.kot.queue.system;

public interface Output {
    String SYSTEM_USE_FACTOR_KEY = "systemUseFactor";
    String AVG_JOB_QUEUE_TIME_KEY = "avgJobQueueTime";
    String AVG_JOB_SYSTEM_TIME_KEY = "avgJobSystemTime";
    String AVG_JOB_QUEUE_NUMBER_KEY = "avgJobQueueNumber";
    String AVG_JOB_SYSTEM_NUMBER_KEY = "avgJobSystemNumber";
    String ABSOLUTE_SYSTEM_THROUGHPUT_KEY = "absTp";
    String RELATIVE_SYSTEM_THROUGHPUT_KEY = "relTp";
}
