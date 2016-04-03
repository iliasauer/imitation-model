package ru.ifmo.kot.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.ui.UiRunner;

public class ModeDispatcher {

    private static final int COMMAND_NOT_FOUND_CODE = 127;
    private static final Logger LOGGER = LogManager.getLogger(ModeDispatcher.class);

    private enum MODE {
        DEFAULT, UI
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            LOGGER.info("The console mode started."); // todo insert console mode running
        } else {
            if (args[0].equalsIgnoreCase(MODE.UI.name())) {
                UiRunner.start();
                LOGGER.info("The UI mode started");
            } else {
                LOGGER.error("There is no command for specified arguments.");
                System.exit(COMMAND_NOT_FOUND_CODE);
            }
        }
    }

}
