package ru.ifmo.kot.queue.system.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Job implements Runnable {

    private static final StringBuilder LOCAL_LOGGER = new StringBuilder();
	private static final Logger LOGGER = LogManager.getLogger(Job.class);

	private static long counter = 1;

    public static void resetCounter() {
        counter = 1;
    }
    public static void resetLogger() {
       LOCAL_LOGGER.setLength(0);
    }


    public static String getLog() {
        String log = LOCAL_LOGGER.toString();
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

	public void reject() {
		state = State.REJECTED;
		printStateDescription(state);
	}

	private String getStateDescription(State state) {
		return  "Job " +
				number() +
				" is " +
				state.name().replace('_', ' ').toLowerCase();
	}

	private void printStateDescription(State state) {
//		System.out.println(getStateDescription(state));
        LOGGER.info(getStateDescription(state));
        LOCAL_LOGGER.append(getStateDescription(state)).append("\n");
	}

	private long number;
	private State state;
	private long workComplexity;

	/**
	 *
	 * @param workComplexity - time of the job execution in seconds
	 */
	public Job(final long workComplexity) {
		this.workComplexity = workComplexity;
		this.number = counter++;
		this.open();
	}

	public long number() {
		return number;
	}

	public State state() {
		return state;
	}

	@Override
	public void run() {
		this.start();
		try {
			TimeUnit.MILLISECONDS.sleep(workComplexity); //TODO TURN BACK!!!
		} catch (InterruptedException e) { // todo try to replace
			e.printStackTrace();
		}
		this.resolve();
		this.close();
	}
}
