package ru.ifmo.kot.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.ui.UiRunner;

import java.net.UnknownHostException;

public class ModeDispatcher {

    private static final int COMMAND_NOT_FOUND_CODE = 127;
    private static final Logger LOGGER = LogManager.getLogger(ModeDispatcher.class);


    private enum MODE {
        DEFAULT, UI
    }

    public static void main(String[] args) {
        final int numberOfArgs = args.length;
        switch (numberOfArgs) {
            case 1:
            case 2:
                if (args[0].equalsIgnoreCase(MODE.UI.name())) {
                    try {
                        UiRunner.start();
                    } catch (UnknownHostException e) {
                        logAndExit("The wrong host address.");
                    }
                    LOGGER.info("The UI mode started");
                } else {
                    logAndExit();
                }
                break;
            case 0:
            case 6:
            case 7:
            case 8:
                try {
                    CliRunner.start(args);
                } catch (IllegalArgumentException e) {
                    logAndExit("Failed to parse arguments");
                }
                break;
            default:
                logAndExit();
        }
    }

    private static void logAndExit() {
        logAndExit("There is no command for specified arguments.");
    }

    private static void logAndExit(final String message) {
        LOGGER.error(message);
        System.exit(COMMAND_NOT_FOUND_CODE);
    }

    private static void logAndExit(final String message, final Throwable t) {
        LOGGER.error(message);
        LOGGER.debug("", t);
        System.exit(COMMAND_NOT_FOUND_CODE);
    }

}
