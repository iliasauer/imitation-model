package ru.ifmo.kot.queue.system.job;

import java.util.concurrent.FutureTask;

public class FutureJob<V> extends FutureTask<V> {

    private Job job;

    public FutureJob(Job job, V result) {
        super(job, result);
        this.job = job;
    }

    public Job getJob() {
        return job;
    }
}
