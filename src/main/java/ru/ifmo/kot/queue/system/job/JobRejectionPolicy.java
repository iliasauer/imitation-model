package ru.ifmo.kot.queue.system.job;

import java.util.concurrent.ThreadPoolExecutor;

public class JobRejectionPolicy extends ThreadPoolExecutor.DiscardPolicy {

    @Override
    public void rejectedExecution(Runnable job, ThreadPoolExecutor e) {
        ((Job) job).reject();
    }
}
