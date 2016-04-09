package ru.ifmo.kot.queue.util.random;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomAuditorTest {

    private static final double[] TEST_SEQUENCE = {
            0.34, 0.56, 0.45, 0.32, 0.54,
            0.22, 0.77, 0.66, 0.53, 0.21};

    @Test
    public void checkIntervals() {
        final double[] intervals = RandomAuditor.equalIntervalsWithStrugesRule(0, 1,
                TEST_SEQUENCE.length);
        printArray(intervals);
    }

    @Test
    public void checkHits() {
        final double[] intervals = RandomAuditor.equalIntervalsWithStrugesRule(0, 1,
                TEST_SEQUENCE.length);
        final int[] hits = RandomAuditor.hits(TEST_SEQUENCE, intervals);
        printArray(hits);
    }

    @Test
    public void checkProbability() {
        final double[] intervals = RandomAuditor.equalIntervals(0, 1, 4);
        final double probability = RandomAuditor.probabilityOfEqualIntervalHit(intervals);
        System.out.println("Probability: " + probability);

    }

    @Test
    public void checkSignificanceCriteriaStatistics() {
        double z = RandomAuditor.significanceCriteriaStatisticsOfEqualIntervalsWithStrugesRule(
                TEST_SEQUENCE, 0, 1, TEST_SEQUENCE.length);
        System.out.println("Z: " + z);
    }

    @Test
    public void checkStrugesRule() {
        assertEquals("The Struges rule works incorrectly",
                14, RandomAuditor.strugesIntervalRule(16));
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