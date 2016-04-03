package ru.ifmo.kot.queue.util.random;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomAuditorTest {

    private static final double[] TEST_SEQUENCE = {
            0.34, 0.56, 0.45, 0.32, 0.54,
            0.22, 0.77, 0.66, 0.53, 0.21};

    @Test
    public void checkIntervals() {
        final double[] intervals = new RandomAuditor().equalIntervals(0, 1, 4);
        printArray(intervals);
    }

    @Test
    public void checkHits() {
        RandomAuditor randomAuditor = new RandomAuditor();
        final double[] intervals = randomAuditor.equalIntervals(0, 1, 4);
        final int[] hits = randomAuditor.hits(TEST_SEQUENCE, intervals);
        printArray(hits);
    }

    @Test
    public void checkSignificanceCriteriaStatistics() {
        RandomAuditor randomAuditor = new RandomAuditor();
        double z = randomAuditor.significanceCriteriaStatisticsOfEqualIntervals(
                TEST_SEQUENCE, 0, 1, 4);
        System.out.println("Z: " + z);
    }

    @Test
    public void checkStrugesRule() {
        RandomAuditor randomAuditor = new RandomAuditor();
        assertEquals("The Struges rule works incorrectly",
                14, randomAuditor.strugesRule(16));
    }

    private void printArray(final double[] array) {
        for (final double each: array) {
            System.out.print(each + " ");
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