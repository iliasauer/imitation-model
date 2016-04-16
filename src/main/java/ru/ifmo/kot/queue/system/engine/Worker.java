package ru.ifmo.kot.queue.system.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Worker extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(Worker.class);
    public static Map<String, Long> STATISTICS = new ConcurrentHashMap<>();

    private long jobProcessTime = 0;

    Worker(ThreadGroup group, Runnable job, String s) {
        super(group, job, s);
    }

    public void increaseTime(final long time) {
        jobProcessTime += time;
        STATISTICS.put(this.getName(), jobProcessTime);
    }

    @Override
    public void run() {
        LOGGER.debug(this.getName() + " starts." );
        super.run();
    }

    @Override
    public void interrupt() {
        LOGGER.debug(this.getName() + " stops." );
        STATISTICS.put(this.getName(), jobProcessTime);
        super.interrupt();
    }

    public static void resetStatistics() {
        STATISTICS.clear();
    }
}
