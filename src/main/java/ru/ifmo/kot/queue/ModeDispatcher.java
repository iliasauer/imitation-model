package ru.ifmo.kot.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ifmo.kot.queue.system.storage.StorageFactory;
import ru.ifmo.kot.queue.ui.UiRunner;

import static ru.ifmo.kot.queue.system.storage.StorageFactory.Discipline.LIFO;

public class ModeDispatcher {

    private static final int COMMAND_NOT_FOUND_CODE = 127;
    private static final Logger LOGGER = LogManager.getLogger(ModeDispatcher.class);


    private enum MODE {
        DEFAULT, UI
    }

    public static void main(String[] args) {
        final int numberOfArgs = args.length;
        switch (numberOfArgs) {
            case 0:
                CommandLineRunner.start(10, 8, 8, LIFO, 60, 180, 2);
                break;
            case 1:
            case 2:
                if (args[0].equalsIgnoreCase(MODE.UI.name())) {
                    UiRunner.start();
                    LOGGER.info("The UI mode started");
                } else {
                    logAndExit();
                }
                break;
            case 7:
                break;
            default:
        }

    }

    private static void logAndExit() {
        LOGGER.error("There is no command for specified arguments.");
        System.exit(COMMAND_NOT_FOUND_CODE);
    }

    private static void logAndExit(final String message) {
        LOGGER.error(message);
        System.exit(COMMAND_NOT_FOUND_CODE);
    }

}
