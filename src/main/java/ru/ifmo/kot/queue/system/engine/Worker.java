package ru.ifmo.kot.queue.system.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Worker extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(Worker.class);

    Worker(ThreadGroup group, Runnable job, String s) {
        super(group, job, s);
    }

    @Override
    public void run() {
        LOGGER.info(this.getName() + " starts." );
        super.run();
        LOGGER.info(this.getName() + " finishes." );
    }
}
