package ru.ifmo.kot.queue.system.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.SystemClock;
import org.apache.logging.log4j.core.util.datetime.FastDateFormat;
import ru.ifmo.kot.queue.system.engine.Worker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Job implements Runnable {

    private static final StringBuffer LOCAL_LOGGER = new StringBuffer();
    private static final Logger LOGGER = LogManager.getLogger(Job.class);
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("HH:mm:ss.SS");
    public static final String IN_QUEUE_TIME = "inQueueTime";
    public static final String IN_SYSTEM_TIME = "inSystemTime";
    public static final Map<Long, Map<String, Long>> STATISTICS = new HashMap<>();

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
        STATISTICS.put(number, new HashMap<String, Long>());
        STATISTICS.get(number).put(IN_QUEUE_TIME, 0L); // by default
        setOpenJobTime();
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
        STATISTICS.get(number).put(IN_SYSTEM_TIME, System.currentTimeMillis() - openJobTime);
        printStateDescription(state);
    }

    void reject() {
        state = State.REJECTED;
        printStateDescription(state);
        STATISTICS.remove(number);
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
    private long startJobTime;
    private long openJobTime;

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
        setStartJobTime();
        Worker worker = (Worker) Thread.currentThread();
        String currentThreadName = worker.getName();
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
        worker.increaseTime(System.currentTimeMillis() - startJobTime);
    }

    private void setOpenJobTime() {
        openJobTime = System.currentTimeMillis();
    }

    public long getOpenJobTime() {
        return openJobTime;
    }

    private void setStartJobTime() {
        startJobTime = System.currentTimeMillis();
    }

    private void logWorkerInfo(final String workerName, final String workerAction) {
        final String info = workerName + " " + workerAction + " executing the job #" + number;
        logInfo(info);
    }

    private void logInfo(final String info) {
        LOCAL_LOGGER.append(DATE_FORMAT.format(System.currentTimeMillis())).append(" - ")
                .append(info).append("\n");
        LOGGER.info(info);
    }
}
