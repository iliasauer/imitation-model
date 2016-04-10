package ru.ifmo.kot.queue.util.random;

import org.junit.Test;
import ru.ifmo.kot.queue.util.math.MathUtil;

import static org.junit.Assert.*;
import static ru.ifmo.kot.queue.util.math.MathUtil.round;
import static ru.ifmo.kot.queue.util.random.RandomAuditor.*;

public class RandomAuditorTest {

    private static final double[] TEST_SEQUENCE = {
            0.34, 0.56, 0.45, 0.32, 0.54,
            0.22, 0.77, 0.66, 0.53, 0.21};

    @Test
    public void checkIntervals() {
        final double[] intervals = equalIntervalsWithStrugesRule(0, 1,
                TEST_SEQUENCE.length);
        printArray(intervals);
    }

    @Test
    public void checkHits() {
        final double[] intervals = equalIntervalsWithStrugesRule(0, 1,
                TEST_SEQUENCE.length);
        final int[] hits = hits(TEST_SEQUENCE, intervals);
        printArray(hits);
    }

    @Test
    public void checkProbability() {
        final double[] intervals = equalIntervals(0, 1, 4);
        final double probability = probabilityOfEqualIntervalHit(intervals);
        System.out.println("Probability: " + probability);

    }

    @Test
    public void checkSignificanceCriteriaStatistics() {
        final int numberOfIntervals = strugesIntervalRule(TEST_SEQUENCE.length);
        final double z = significanceCriteriaStatisticsOfEqualIntervals(TEST_SEQUENCE, 0, 1,
                        numberOfIntervals);
        double chi2quantile = chiSquaredDistributionQuantile(numberOfIntervals - 1);
        System.out.println("Z: " + round(z, 4));
        System.out.println("chi^2 quantile: " + round(chi2quantile, 4));
        assertTrue(z < chi2quantile);
    }

    @Test
    public void checkStrugesRule() {
        assertEquals("The Struges rule works incorrectly",
                14, strugesIntervalRule(16));
    }


    private void printArray(final double[] array) {
        for (final double each: array) {
            System.out.println(each + " ");
        }
        System.out.println();
    }

    private void printArray(final int[] array) {
        for (final int each: array) {
            System.out.print(each + " ");
        }
        System.out.println();
    }

}