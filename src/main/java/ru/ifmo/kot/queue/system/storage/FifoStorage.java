package ru.ifmo.kot.queue.system.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.job.FutureJob;
import ru.ifmo.kot.queue.system.job.Job;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FifoStorage<T> extends LinkedBlockingQueue<T> {

    private static final Logger LOGGER = LogManager.getLogger(FifoStorage.class);

    FifoStorage(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(T item) {
        jobInfo(item, "Trying to add", "to", false);
        return super.offer(item);
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        T item = super.poll(timeout, unit);
        jobInfo(item, "Taking", "from", true);
        return item;
    }

    @Override
    public T take() throws InterruptedException {
        T item = super.take();
        jobInfo(item, "Taking", "from", true);
        return item;
    }

    @SuppressWarnings("Duplicates")
    private void jobInfo(final T item, final String action, final String direction, boolean stat) {
        if (item instanceof FutureJob) {
            final Job job = ((FutureJob) item).getJob();
            final long jobNumber = job.number();
            if (stat) {
                Job.STATISTICS.get(jobNumber).put(
                        Job.IN_QUEUE_TIME, System.currentTimeMillis() - job.getOpenJobTime());
            }
            LOGGER.debug(action + " the job #" + jobNumber + " "  + direction + " the storage");
        }
    }

}
