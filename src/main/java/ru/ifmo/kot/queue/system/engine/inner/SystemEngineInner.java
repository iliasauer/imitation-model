package ru.ifmo.kot.queue.system.engine.inner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemEngineInner {

    public SystemEngineInner() {
        Logger logger = LogManager.getLogger(SystemEngineInner.class);
        logger.trace("SystemEngineInner trace message");
        logger.error("SystemEngineInner error message");
    }

}
