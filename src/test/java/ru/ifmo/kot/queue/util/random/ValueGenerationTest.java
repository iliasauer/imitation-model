package ru.ifmo.kot.queue.util.random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.runners.Parameterized.*;

@SuppressWarnings("WeakerAccess")
@RunWith(value = Parameterized.class)
public class ValueGenerationTest {

    private static final double PERCENTAGE = 0.01;

    @Parameters
    public static Iterable data() {
        return Arrays.asList(60, 180);
    }

    @Parameter
    public int value;

    @Test
    public void checkMeanOfRangeUniformlyDistributedRandomSequence() {
        double[] sequence = ComplexRandom.rangeExponentialDistributedRandomSequence(value *
                2);
        double sqrOfValue = Math.pow(value, 2);
        double mean = ComplexRandom.mean(sequence);
        double variance = ComplexRandom.variance(sequence);
        double deltaMean = round(((mean - value) / value), 4);
        double deltaVariance = round(((variance - sqrOfValue) / sqrOfValue), 4);
        System.out.println("Value: " + value);
        System.out.println("The mean estimation: " + mean
                + " (delta = " + deltaMean + "%)");
        System.out.println("The variance estimation: " + variance
                + " (delta = " + deltaVariance + "%)");
        Assert.assertEquals(value, mean, value * PERCENTAGE);
        Assert.assertEquals(sqrOfValue, variance, sqrOfValue * PERCENTAGE);
    }

    private static double round(final double value, final int numberOfDigits) {
        double rounder = 1.0;
        for (int i = 0; i < numberOfDigits; i++) {
            rounder *= 10.0;
        }
        return Math.round(value * rounder) / rounder;
    }
}
