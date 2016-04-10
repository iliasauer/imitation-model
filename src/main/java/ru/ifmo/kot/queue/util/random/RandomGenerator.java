package ru.ifmo.kot.queue.util.random;

import static java.lang.Math.log;

@SuppressWarnings("WeakerAccess") // for tests
public class RandomGenerator {

    /**
     * start values
     */
    public static final int SEED_1 = 676475;
    public static final int SEED_2 = 565645;

    /**
     * the number of values the sequence is formed of
     */
    public static final int m = 2_147_483_647;
    /**
     * "multiplier"
     */
    public static final int a = 630_360_016;

    private static final int minValue = 1;

    private double prevValue;
    private int range;

    private RandomGenerator(final int seed, final int range) {
        this.prevValue = seed;
        this.range = range;
    }

    private double nextValue(final double number) {
        return (a * number) % m;
    }

    private double normalizeValue(final double value) {
        return value / m;
    }

    private double  exponizedValue(final double value) {
        return  -0.5 * log(value);
    }

    private double nextDouble(int range) {
        prevValue = nextValue(prevValue);
        double value = exponizedValue(normalizeValue(prevValue));
        value = minValue + range * value;
        return value;
    }

    public int nextInt() {
        return (int) nextDouble(range);
    }

    public double[] generateDoubleSequence(final int numberOfValues) {
        double[] sequence = new double[numberOfValues];
        for (int i = 0; i < numberOfValues; i++) {
            sequence[i] = nextDouble(range - minValue);
        }
        return sequence;
    }

    public int[] generateIntSequence(final int numberOfValues) {
        int[] sequence = new int[numberOfValues];
        for (int i = 0; i < numberOfValues; i++) {
            sequence[i] = nextInt();
        }
        return sequence;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        // default values
        private int seed = ComplexRandom.SEED_1;
        private int range = 60;

        private Builder() {
        }

        public Builder setSeed(final int seed) {
            this.seed = seed;
            return this;
        }

        public Builder setGoal(final int goal) {
            this.range = (goal * 2);
            return this;
        }

        public RandomGenerator build() {
            return new RandomGenerator(seed, range);
        }

    }

}
