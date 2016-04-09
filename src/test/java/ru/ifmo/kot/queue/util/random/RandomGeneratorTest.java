package ru.ifmo.kot.queue.util.random;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomGeneratorTest {

    private static final double PERCENTAGE = 0.01;

    @Test
    public void nextDouble() {
        final int goal1 = 60;
        final int goal2 = 120;
        RandomGenerator.Builder generatorBuilder = RandomGenerator.newBuilder();
        generatorBuilder.setSeed(ComplexRandom.SEED_1);
        generatorBuilder.setGoal(goal1);
        RandomGenerator generator1 = generatorBuilder.build();
        generatorBuilder.setGoal(goal2);
        RandomGenerator generator2 = generatorBuilder.build();
        final int n = 1000;
        double[] sequence = generateSequence(generator1, n);
        double mean = ComplexRandom.mean(sequence);
        assertEquals(goal1, mean, goal1 * PERCENTAGE);
        sequence = generateSequence(generator2, n);
        mean = ComplexRandom.mean(sequence);
        assertEquals(goal2, mean, goal2 * PERCENTAGE);
    }

    private double[] generateSequence(final RandomGenerator generator, final int numberOfValues) {
        double[] sequence = new double[numberOfValues];
        for (int i = 0; i < numberOfValues; i++) {
            sequence[i] = generator.nextInt();
        }
        return sequence;
    }

}