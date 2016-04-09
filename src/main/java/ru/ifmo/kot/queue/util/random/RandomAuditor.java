package ru.ifmo.kot.queue.util.random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.pow;

@SuppressWarnings("WeakerAccess") // for tests
public class RandomAuditor {

    public static double[] equalIntervals(final double minValue, final double maxValue,
                                  final int numberOfIntervals) {

        double[] intervals = new double[numberOfIntervals + 1];
        intervals[0] = minValue;
        intervals[numberOfIntervals] = maxValue;
        final double width = (maxValue - minValue) / numberOfIntervals;
        for (int i = 1; i < intervals.length - 1; i++) {
            intervals[i] = intervals[i - 1] + width;
        }
        return intervals;
    }

    public static double[] equalIntervalsWithStrugesRule(final double minValue, final double
            maxValue,
                                                  final int numberOfValues) {
        return equalIntervals(minValue, maxValue, strugesIntervalRule(numberOfValues));
    }

    public static double[] equalIntervalsWithEmpiricRule(final double minValue, final double
            maxValue,
                                                  final int numberOfValues) {
        return equalIntervals(minValue, maxValue, empiricIntervalRule(numberOfValues));
    }

    public static double probabilityOfEqualIntervalHit(final double[] intervals) {
        return (intervals[1] - intervals[0])/(intervals[intervals.length - 1] - intervals[0]);
    }

    public static int[] hits(final double[] sequence,
                                  final double[] intervals) {
        int[] hits = new int[intervals.length - 1];
        for (int i = 0; i < hits.length; i++) {
            hits[i] = 0;
        }
        for (double value: sequence) {
            for (int i = 0; i < intervals.length - 1; i++) {
                if (value > intervals[i] && value <= intervals[i + 1]) {
                    hits[i] += 1;
                }
            }
        }
        return hits;
    }

    public static double[] frequencyOfHits(final double[] sequence, final double[] intervals) {
        int[] hits = hits(sequence, intervals);
        int n = sequence.length;
        double[] frequency = new double[hits.length];
        for (int i = 0; i < hits.length; i++) {
            frequency[i] = (double) hits[i] / n;
        }
        return frequency;
    }

    public static double[] heightsOfBarRectangles(final double[] sequence,
                                                  final double[] intervals) {
        double[] frequency = frequencyOfHits(sequence, intervals);
        double deltaX = intervals[1] - intervals[0];
        double[] heights = new double[frequency.length];
        for (int i = 0; i < heights.length; i++) {
            heights[i] = frequency[i] / deltaX;
        }
        return heights;
    }

    // Zi = ((ni - n * pi)^2) / (n * pi)
    // in this case pi is a constant
    public static double significanceCriteriaStatisticsOfEqualIntervals(
            final double[] sequence,
            final double minValue, final double maxValue,
            final int numberOfIntervals) {
        double[] intervals = equalIntervals(minValue, maxValue, numberOfIntervals);
        double probabilityOfHit = probabilityOfEqualIntervalHit(intervals);
        int[] hits = hits(sequence, intervals);
        double z = 0;
        double constantPart = sequence.length * probabilityOfHit;
        for (int anIntervalHits : hits) {
            z += pow((anIntervalHits - constantPart), 2) / constantPart;
        }
        return z;
    }

    public static double significanceCriteriaStatisticsOfEqualIntervalsWithStrugesRule(
            final double[] sequence,
            final double minValue, final double maxValue,
            final int numberOfValues) {
        double[] intervals = equalIntervalsWithStrugesRule(minValue, maxValue, numberOfValues);
        double probabilityOfHit = probabilityOfEqualIntervalHit(intervals);
        int[] hits = hits(sequence, intervals);
        double z = 0;
        double constantPart = sequence.length * probabilityOfHit;
        for (int anIntervalHits : hits) {
            z += pow((anIntervalHits - constantPart), 2) / constantPart;
        }
        return z;
    }

    // The logarithm to base 2
    private static double lg(double num) {
        return Math.log(num)/Math.log(2);
    }

    public static int strugesIntervalRule(int numberOfValues) {
        return 1 + (int) (3.3 * lg(numberOfValues));
    }

    public static int empiricIntervalRule(int numberOfValues) {
        return (int) (1.72 + Math.cbrt(numberOfValues));
    }
}
