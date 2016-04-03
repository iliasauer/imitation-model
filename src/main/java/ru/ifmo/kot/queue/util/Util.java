package ru.ifmo.kot.queue.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Util {

    public Util() {
        Logger logger = LogManager.getLogger(Util.class);
        logger.error("Util error message");
    }
}
