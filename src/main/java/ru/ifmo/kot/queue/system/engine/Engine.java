package ru.ifmo.kot.queue.system.engine;

import ru.ifmo.kot.queue.system.job.FutureJob;
import ru.ifmo.kot.queue.system.job.Job;
import ru.ifmo.kot.queue.system.job.JobRejectionPolicy;
import ru.ifmo.kot.queue.system.storage.StorageFactory;
import ru.ifmo.kot.queue.system.storage.Discipline;

import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Engine extends ThreadPoolExecutor {

    private static final ThreadFactory WORKER_FACTORY = new WorkerFactory();

    @SuppressWarnings("unchecked")
    public Engine(int numberOfWorkers, int capacityOfStorage, Discipline discipline) {
        super(numberOfWorkers, numberOfWorkers, 0L, TimeUnit.MILLISECONDS,
                StorageFactory.createStorage(discipline, capacityOfStorage),
                WORKER_FACTORY, new JobRejectionPolicy());
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable job, T value) {
        return new FutureJob<>((Job) job, value);
    }
}
