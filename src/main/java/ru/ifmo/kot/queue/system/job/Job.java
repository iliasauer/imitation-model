package ru.ifmo.kot.queue.system.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.datetime.FastDateFormat;

import java.util.concurrent.TimeUnit;

public class Job implements Runnable {

    private static final StringBuffer LOCAL_LOGGER = new StringBuffer();
    private static final Logger LOGGER = LogManager.getLogger(Job.class);
    private static final FastDateFormat dateFormat = FastDateFormat.getInstance("HH:mm:ss.SS");


    private static long jobCounter = 1;
    private static long runsCounter = 1;

    public static void resetJobCounter() {
        jobCounter = 1;
        runsCounter++;
    }

    public static void resetRunsCounter() {
        runsCounter = 1;
    }

    private static void resetLogger() {
        LOCAL_LOGGER.setLength(0);
    }

    public static String getLog() {
        String log = LOCAL_LOGGER.toString();
        resetLogger();
        return log;
    }

    private enum State {
        OPEN, IN_PROGRESS, RESOLVED, CLOSED, REJECTED
    }

    private void open() {
        state = State.OPEN;
        printStateDescription(state);
    }

    private void start() {
        state = State.IN_PROGRESS;
        printStateDescription(state);
    }

    private void resolve() {
        state = State.RESOLVED;
        printStateDescription(state);
    }

    private void close() {
        state = State.CLOSED;
        printStateDescription(state);
    }

    void reject() {
        state = State.REJECTED;
        printStateDescription(state);
    }

    private String getStateDescription(State state) {
        return "Job " +
                number() +
                " is " +
                state.name().replace('_', ' ').toLowerCase();
    }

    private void printStateDescription(State state) {
//		System.out.println(getStateDescription(state));
        if (number() == 1 && state.equals(State.OPEN)) {
            LOCAL_LOGGER.append("The run #").append(runsCounter).append(".\n");
        }
        logInfo(getStateDescription(state));
    }



    private long number;
    private State state;
    private long complexity;

    /**
     * @param complexity - time of the job execution in seconds
     */
    public Job(final long complexity) {
        this.complexity = complexity;
        this.number = jobCounter++;
        this.open();
    }

    public long number() {
        return number;
    }

    @SuppressWarnings("unused")
    public State state() {
        return state;
    }

    @Override
    public void run() {
        String currentThreadName = Thread.currentThread().getName();
        logWorkerInfo(currentThreadName, "starts");
        this.start();
        try {
            TimeUnit.MILLISECONDS.sleep(complexity);
        } catch (InterruptedException e) {
            LOGGER.error("The internal server error");
        }
        this.resolve();
        this.close();
        logWorkerInfo(currentThreadName, "finishes");
    }

    private void logWorkerInfo(final String workerName, final String workerAction) {
        final String info = workerName + " " + workerAction + " executing the job #" + number;
        logInfo(info);
    }

    private void logInfo(final String info) {
        LOCAL_LOGGER.append(dateFormat.format(System.currentTimeMillis())).append(" - ")
                .append(info).append("\n");
        LOGGER.info(info);
    }
}
