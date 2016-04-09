package ru.ifmo.kot.queue.util.random;

import static ru.ifmo.kot.queue.util.random.ComplexRandom.*;

public class RandomGenerator {

    private static final int minValue = 0;

    private int goal;
    private int range;
    private double prevValue;

    public RandomGenerator(final int seed, final int goal) {
        this.prevValue = seed;
        this.goal = goal;
        this.range = (goal * 2) - minValue;
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


}
