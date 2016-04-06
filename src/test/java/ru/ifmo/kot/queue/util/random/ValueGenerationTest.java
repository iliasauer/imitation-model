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
        double[] sequence = ComplexRandom.exponentialyDistributedRandomSequence();
        final double sqrOfValue = Math.pow(value, 2);
        final double mean = ComplexRandom.mean(sequence);
        final double variance = ComplexRandom.variance(sequence);
        final double deltaMean = round(((mean - value) / value), 4);
        final double deltaVariance = round(((variance - sqrOfValue) / sqrOfValue), 4);
        final double[] confidenceInterval = ComplexRandom.confidenceInterval(sequence);
        System.out.println("Value: " + value);
        System.out.println("The mean estimation: " + round(mean, 4)
                + " (delta = " + deltaMean + "%)");
        System.out.println("The variance estimation: " + round(variance, 4)
                + " (delta = " + deltaVariance + "%)");
        System.out.println("The confidence interval: [" + round(confidenceInterval[0], 4) +
        "; " + round(confidenceInterval[1], 4) + "]");
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
