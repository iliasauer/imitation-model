package ru.ifmo.kot.queue.system.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.job.FutureJob;
import ru.ifmo.kot.queue.system.job.Job;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FifoStorage<T> extends LinkedBlockingQueue<T> {

    private static final Logger LOGGER = LogManager.getLogger(FifoStorage.class);

    public FifoStorage(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(T item) {
        logJobInfo(item, "Trying to add", "to");
        return super.offer(item);
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        T item = super.poll(timeout, unit);
        logJobInfo(item, "Taking", "from");
        return item;
    }

    @Override
    public T take() throws InterruptedException {
        T item = super.take();
        logJobInfo(item, "Taking", "from");
        return item;
    }

    private void logJobInfo(final T item, final String action, final String direction) {
        if (item instanceof FutureJob) {
            final Job job = ((FutureJob) item).getJob();
            LOGGER.info(action + " the job#" + job.number() + " "  + direction + " the storage");
        }
    }

}
