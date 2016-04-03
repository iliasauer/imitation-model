package ru.ifmo.kot.queue.system.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemEngine {

    public SystemEngine() {
        Logger logger = LogManager.getLogger(SystemEngine.class);
        logger.trace("SystemEngine trace message");
    }
}
