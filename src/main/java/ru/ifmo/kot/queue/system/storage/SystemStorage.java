package ru.ifmo.kot.queue.system.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemStorage {

    public SystemStorage() {
        Logger logger = LogManager.getLogger(SystemStorage.class);
        logger.trace("SystemStorage trace message");
    }
}
