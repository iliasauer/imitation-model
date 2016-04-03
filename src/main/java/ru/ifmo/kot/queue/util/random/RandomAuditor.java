package ru.ifmo.kot.queue.util.random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.pow;

@SuppressWarnings("WeakerAccess") // for tests
public class RandomAuditor {

    private static final Logger LOGGER = LogManager.getLogger(RandomAuditor.class);

    public double[] equalIntervals(final double minValue, final double maxValue,
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

    public double probabilityOfEqualIntervalHit(final double[] intervals) {
        return intervals[1] - intervals[0];
    }

    public int[] hits(final double[] sequence,
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

    // Zi = ((ni - n * pi)^2) / (n * pi)
    // in this case pi is a constant
    public double significanceCriteriaStatisticsOfEqualIntervals(
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

    // The logarithm to base 2
    private double lg(double num) {
        return Math.log(num)/Math.log(2);
    }

    public int strugesRule(int numberOfValues) {
        return 1 + (int) (3.3 * lg(numberOfValues));
    }
}
