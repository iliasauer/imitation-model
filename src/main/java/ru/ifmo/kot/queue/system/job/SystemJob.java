package ru.ifmo.kot.queue.system.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemJob {

    public SystemJob() {
        Logger logger = LogManager.getLogger(SystemJob.class);
        logger.trace("SystemJob trace message");
    }
}
