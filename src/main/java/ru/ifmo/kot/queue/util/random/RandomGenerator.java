package ru.ifmo.kot.queue.util.random;

import static ru.ifmo.kot.queue.util.random.ComplexRandom.*;

public class RandomGenerator {

    private static final int minValue = 0;

    private double prevValue;
    private int range;

    private RandomGenerator(final int seed, final int goal) {
        this.prevValue = seed;
        this.range = range(goal);
    }

    private int range(final int goal) {
        return (goal * 2) - minValue;
    }

    private double nextDouble() {
        prevValue = ComplexRandom.nextValue(prevValue);
        double value = exponizedValue(normalizeValue(prevValue), 0.5);
        value = minValue + range * value;
        return value;
    }

    public int nextInt() {
        return (int) nextDouble();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        // default values
        private int seed = ComplexRandom.SEED_1;
        private int goal = 60;

        private Builder() {
        }

        public Builder setSeed(final int seed) {
            this.seed = seed;
            return this;
        }

        public Builder setGoal(final int goal) {
            this.goal = goal;
            return this;
        }

        public RandomGenerator build() {
            return new RandomGenerator(seed, goal);
        }

    }

}
