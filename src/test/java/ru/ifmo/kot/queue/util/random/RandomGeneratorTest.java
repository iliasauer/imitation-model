package ru.ifmo.kot.queue.util.random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class RandomGeneratorTest {

    private static final double PERCENTAGE = 0.01;

    @Parameterized.Parameters
    public static Iterable data() {
        return Arrays.asList(60, 180);
    }

    @Parameterized.Parameter
    public int goal;

    @Test
    public void nextDouble() {
        final int n = 1000000;
        RandomGenerator.Builder generatorBuilder = RandomGenerator.newBuilder();
        generatorBuilder.setSeed(RandomGenerator.SEED_1);
        generatorBuilder.setGoal(goal);
        RandomGenerator generator = generatorBuilder.build();
        double[] doubleSequence = generator.generateDoubleSequence(n);
        int[] intSequence = generator.generateIntSequence(n);
        for (int x: intSequence) {
            assertTrue("The value must be be greater than or equal to 1", x >= 1);
        }
        assertEquals(goal, ComplexRandom.mean(doubleSequence), goal * PERCENTAGE);
        assertEquals(goal, ComplexRandom.mean(intSequence), 0);
        System.out.println(ComplexRandom.mean(ComplexRandom.rangeExponentialyDistributedRandomSequence
                (RandomGenerator.SEED_1, 1, goal * 2 - 1)));
    }



}