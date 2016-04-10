package ru.ifmo.kot.queue.system.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Worker extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(Worker.class);
    public static final Map<String, Long> STATISTICS = new HashMap<>();

    private long jobProcessTime = 0;

    Worker(ThreadGroup group, Runnable job, String s) {
        super(group, job, s);
    }

    public void increaseTime(final long time) {
        jobProcessTime += time;
    }

    @Override
    public void run() {
        LOGGER.info(this.getName() + " starts." );
        super.run();
    }

    @Override
    public void interrupt() {
        LOGGER.info(this.getName() + " stops." );
        STATISTICS.put(this.getName(), jobProcessTime);
        super.interrupt();
    }
}
