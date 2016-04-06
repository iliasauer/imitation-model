package ru.ifmo.kot.queue.util.random;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;

import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;
import static ru.ifmo.kot.queue.util.random.ComplexRandom.*;

public class ComplexRandomTest {

    private static final double DOUBLE_DELTA = 0.00001;
    private static final int commonSeed = SEED_1;
    private static final double[] commonSequence =
            standardUniformlyDistributedRandomSequence(commonSeed);

    @Test
    public void checkGenerationOfValue() {
        // values were obtained empirically
        final double currentValue = 676475.0;
        final double expectedNextValue = 1.259006104e+9;

        final double actualNextValue = nextValue(currentValue);

        assertEquals("The next value was generated wrong.",
                expectedNextValue, actualNextValue, DOUBLE_DELTA);
    }

    @Test
    public void checkNormalizationOfValue() {
        // values were obtained empirically
        final double currentValue = 676475.0;
        final double expectedNormalizedValue = 3.150082194781901e-4;

        final double actualNormalizedValue = normalizeValue(currentValue);

        assertEquals("The value was normalized wrong.",
                expectedNormalizedValue, actualNormalizedValue, DOUBLE_DELTA);
    }

    @Test
    public void checkUniformlyDistributedRandomSequence() {

        final double[] uniDistRandSeq =
                uniformlyDistributedRandomSequence(SEED_1, a, m, n);

        assertEquals("Even the first value does not equal the seed",
                uniDistRandSeq[0], SEED_1, DOUBLE_DELTA);

        final int indexOfCheck = 65;
        assertEquals("The value " + "#" + indexOfCheck + " does not equal the checked value",
                uniDistRandSeq[indexOfCheck],
                iterativeNextValue(indexOfCheck, SEED_1),
                DOUBLE_DELTA);
    }

    @Test
    public void checkStandardUniformlyDistributedRandomSequence() {
        final double[] uniDistRandSeq =
                uniformlyDistributedRandomSequence(SEED_1, a, m, n);
        final double[] stdUniDistRandSeq =
                standardUniformlyDistributedRandomSequence(SEED_1, a, m, n);

        int indexOfCheck = 999;
        assertEquals("The value " + "#" + indexOfCheck + "was normalized wrong",
                stdUniDistRandSeq[indexOfCheck],
                normalizeValue(uniDistRandSeq[indexOfCheck]),
                DOUBLE_DELTA);
    }

    @Test
    public void checkMean() {
        final double expectedMean = new Mean().evaluate(commonSequence);
        final double actualMean = mean(commonSequence);
        assertEquals("The mean was calculated wrong",
                expectedMean,
                actualMean,
                DOUBLE_DELTA);
    }

    @Test
    public void checkVariance() {
        final double expectedVariance = new Variance().evaluate(commonSequence);
        final double actualVariance = variance(commonSequence);
        assertEquals("The variance was calculated wrong",
                expectedVariance,
                actualVariance,
                DOUBLE_DELTA);
    }

    @Test
    public void checkStandardDeviation() {
        final double expectedSd = new StandardDeviation().evaluate(commonSequence);
        final double actualSd = standardDeviation(commonSequence);
        assertEquals("The standard deviation was calculated wrong",
                expectedSd,
                actualSd,
                DOUBLE_DELTA);
    }

// todo fix it
//    @Test
//    public void checkCovariance() {
//        // values were obtained empirically
//        final int seed = 676475;
//        final int indexOfCheck = 998;
//        final double expectedValue = -0.154098281730902; // for the index that specified above
//
//        final double[] stdUniDistRandSeq =
//                standardUniformlyDistributedRandomSequence(seed);
//
//        assertEquals("The value " + "#" + indexOfCheck + " does not equal the checked value",
//                expectedValue,
//                covariance(stdUniDistRandSeq)[indexOfCheck],
//                DOUBLE_DELTA);
//    }

//    @Test
//    public void checkCorrelation() {
//        // values were obtained empirically
//        final int seed = 676475;
//        final int firstIndexOfCheck = 0;
//        final double firstExpectedValue = 0.016064724191234; // for the index that specified above
//        final int secondIndexOfCheck = 998;
//        final double secondExpectedValue = -1.738026692492030; // for the index that specified above
//
//        final double[] stdUniDistRandSeq =
//                standardUniformlyDistributedRandomSequence(seed);
//        final double[] actualCorrelation = correlation(stdUniDistRandSeq);
//
//        assertEquals("The value " + "#" + firstIndexOfCheck + " does not equal the checked value",
//                firstExpectedValue,
//                actualCorrelation[firstIndexOfCheck],
//                DOUBLE_DELTA);
//
//        assertEquals("The value " + "#" + secondIndexOfCheck + " does not equal the checked value",
//                secondExpectedValue,
//                actualCorrelation[secondIndexOfCheck],
//                DOUBLE_DELTA);
//    }

    @Test
    public void checkInterval() {
        final double[] actualInterval = interval(commonSequence);
        System.out.println("Interval: [" + actualInterval[0] + ", " + actualInterval[1] + "]");
    }

    @Test
    public void checkHypothesis() {
        final double n = commonSequence.length;
        final double mean = mean(commonSequence);
        final double sd = standardDeviation(commonSequence);
        final double accuracy = accuracy(commonSequence);

        // significance criterion statistics
        final double z = sqrt(n) * (mean - accuracy) / sd;
        System.out.println("n: " + n);
        System.out.println("Mean: " + mean);
        System.out.println("Standard deviation: " + sd);
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Z: " + z);
    }

    @Test
    public void checkNonStaticStandardUniformlyDistributedRandomSequence() {
        final ComplexRandom random = new ComplexRandom(SEED_1, Type.STANDARD);
        assertEquals("The mean was calculated wrong",
                new Mean().evaluate(random.sequence()), random.mean(), DOUBLE_DELTA);
        assertEquals("The variance was calculated wrong",
                new Variance().evaluate(random.sequence()), random.variance(), DOUBLE_DELTA);
    }

//////////////////////auxiliary methods////////////////////////

    private double iterativeNextValue(int index, double seed) {
        double value = seed;
        for (int i = 0; i < index; i++) {
            value = nextValue(value);
        }
        return value;
    }

}