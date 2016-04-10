package ru.ifmo.kot.queue.system.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadPoolExecutor;

public class JobRejectionPolicy extends ThreadPoolExecutor.DiscardPolicy {

    private static final Logger LOGGER = LogManager.getLogger(Job.class);

    @Override
    public void rejectedExecution(Runnable futureJob, ThreadPoolExecutor e) {
        Job job = ((FutureJob) futureJob).getJob();
        job.reject();
        LOGGER.info("The job #" + job.number() + " was rejected");
    }
}
