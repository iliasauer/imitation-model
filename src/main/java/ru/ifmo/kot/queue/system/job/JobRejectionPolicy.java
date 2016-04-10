package ru.ifmo.kot.queue.system.job;

import java.util.concurrent.ThreadPoolExecutor;

public class JobRejectionPolicy extends ThreadPoolExecutor.DiscardPolicy {

    @Override
    public void rejectedExecution(Runnable futureJob, ThreadPoolExecutor e) {
        Job job = ((FutureJob) futureJob).getJob();
        job.reject();
    }
}
