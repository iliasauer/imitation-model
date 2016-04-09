package ru.ifmo.kot.queue.util.random;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomGeneratorTest {

    private static final double PERCENTAGE = 0.01;

    @Test
    public void nextDouble() {
        final int goal = 60;
        RandomGenerator generator = new RandomGenerator(ComplexRandom.SEED_1, goal);
        final int n = 1000;
        double[] sequence = new double[n];
        for (int i = 0; i < n; i++) {
            sequence[i] = generator.nextInt();
        }
        assertEquals(goal, ComplexRandom.mean(sequence), goal * PERCENTAGE);
    }

}