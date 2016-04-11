package ru.ifmo.kot.queue.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

public class UiRunner implements Runnable {

    private static final int DEFAULT_PORT = 8080;
    private static final Logger LOGGER = LogManager.getLogger(UiRunner.class);
    private static final String WEBAPP_DIR_LOCATION =
            "src/main/webapp/";
    private static final String WEBXML_LOCATION =
            WEBAPP_DIR_LOCATION + "WEB-INF/web.xml";
    private static final String CONTEXT_PATH = "/";
    private static boolean hasBeenStarted = false;

    static {
        Log.setLog(new org.eclipse.jetty.util.log.Logger() {
            @Override
            public String getName() {
                return "FakeLogger";
            }

            @Override
            public void warn(String msg, Object... args) {

            }

            @Override
            public void warn(Throwable thrown) {

            }

            @Override
            public void warn(String msg, Throwable thrown) {

            }

            @Override
            public void info(String msg, Object... args) {

            }

            @Override
            public void info(Throwable thrown) {

            }

            @Override
            public void info(String msg, Throwable thrown) {

            }

            @Override
            public boolean isDebugEnabled() {
                return false;
            }

            @Override
            public void setDebugEnabled(boolean enabled) {

            }

            @Override
            public void debug(String msg, Object... args) {

            }

            @Override
            public void debug(String msg, long value) {

            }

            @Override
            public void debug(Throwable thrown) {

            }

            @Override
            public void debug(String msg, Throwable thrown) {

            }

            @Override
            public org.eclipse.jetty.util.log.Logger getLogger(String name) {
                return this;
            }

            @Override
            public void ignore(Throwable ignored) {

            }
        }); // to remove extra messages
    }

    private Server server = null;

    private UiRunner(int port) {
        server = new Server(port);
    }


    @SuppressWarnings("WeakerAccess") // for future
    public static void start(int portNumber) {
        if (!hasBeenStarted) {
            new UiRunner(portNumber).run();
            hasBeenStarted = true;
        } else {
            LOGGER.error("The UI mode has already started");
        }
    }

    public static void start() {
        start(DEFAULT_PORT);
    }

    @Override
    public void run() {

        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath(CONTEXT_PATH);
        ctx.setDescriptor(WEBXML_LOCATION);
        ctx.setResourceBase(WEBAPP_DIR_LOCATION);
        ctx.setParentLoaderPriority(true);

        server.setHandler(ctx);
        try {
            server.start();
            LOGGER.debug("The UI server started.");
        } catch (Exception e) {
            LOGGER.debug("The UI server cannot started.", e);
        }
    }


}
