package ru.ifmo.kot.queue.util.random;


import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


/**
 * Note: The author has to use double type for all generators, because
 * the correctness of the generators is determined by using MatLab
 */
@SuppressWarnings("WeakerAccess")
public class ComplexRandom {

    /**
     * start values
     */
    public static final int SEED_1 = 676475;
    @SuppressWarnings("unused")
    public static final int SEED_2 = 565645;

    /**
     * the number of values the sequence is formed of
     */
    public static final int m = 2_147_483_647;
    /**
     * "multiplier"
     */
    public static final int a = 630_360_016;
    /**
     * the number of sequence values
     */
    public static final int n = 50_000;

    /**
     * the quantile of a standard normal distribution of order of "1 - α/2"
     * α=1-β - a significance level (α = 0.05)
     */
    public static final double u = 1.96;


    @SuppressWarnings("unused")
    public enum Type {
        STANDARD, EXPONENTIAL
    }

    public static double nextValue(final double number) {
        return nextValue(number, a, m);
    }

    private static double nextValue(final double number, final int a, final int m) {
        return (a * number) % m;
    }

    public static double normalizeValue(final double value) {
        return normalizeValue(value, m);
    }

    private static double normalizeValue(final double value, final int m) {
        return value / m;
    }

    public static double  exponizedValue(final double value, final double mean) {
        return  -mean * log(value);
    }

    public static double[] uniformlyDistributedRandomSequence(
            final int seed, final int a,
            final int m, final int n) {
        double[] sequence = new double[n];
        sequence[0] = seed;
        for (int i = 1; i < n; i++) {
            sequence[i] = nextValue(sequence[i - 1], a, m);
        }
        return sequence;
    }

    public static double[] uniformlyDistributedRandomSequence(final int seed) {
        return uniformlyDistributedRandomSequence(seed, a, m, n);
    }

    @SuppressWarnings("unused")
    public static double[] uniformlyDistributedRandomSequence() {
        return uniformlyDistributedRandomSequence(SEED_1);
    }

    public static double[] standardUniformlyDistributedRandomSequence(
            final int seed, final int a,
            final int m, final int n) {
        double[] prevSequence = uniformlyDistributedRandomSequence(seed, a, m, n);
        double[] sequence = new double[n];
        for (int i = 0; i < n; i++) {
            sequence[i] = normalizeValue(prevSequence[i]);
        }
        return sequence;
    }

    public static double[] standardUniformlyDistributedRandomSequence(final int seed) {
        return standardUniformlyDistributedRandomSequence(seed, a, m, n);
    }

    @SuppressWarnings("unused")
    public static double[] standardUniformlyDistributedRandomSequence() {
        return standardUniformlyDistributedRandomSequence(SEED_1, a, m, n);
    }

    public static double[] exponentialyDistributedRandomSequence(final int seed, final int a,
                                              final int m, final int n) {
        double[] prevSequence = standardUniformlyDistributedRandomSequence(seed, a, m, n);
        double mean = mean(prevSequence);
        double[] sequence = new double[n];
        for (int i = 0; i < n; i++) {
            sequence[i] = exponizedValue(prevSequence[i], mean);
        }
        return sequence;
    }

    public static double[] exponentialyDistributedRandomSequence(final int seed) {
        return exponentialyDistributedRandomSequence(seed, a, m, n);
    }

    @SuppressWarnings("unused")
    public static double[] exponentialyDistributedRandomSequence() {
        return exponentialyDistributedRandomSequence(SEED_1, a, m, n);
    }

    public static double[] rangeExponentialyDistributedRandomSequence(final int seed, final int
            minValue, final int maxValue) {
        double[] prevSequence = exponentialyDistributedRandomSequence(seed);
        double[] sequence = new double[prevSequence.length];
        int range = maxValue - minValue;
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = minValue + range * prevSequence[i];
        }
        return sequence;
    }

    public static double[] rangeExponentialyDistributedRandomSequence(final int
                                                                                      minValue, final int maxValue) {
        return rangeExponentialyDistributedRandomSequence(SEED_1, minValue, maxValue);
    }

    public static double[] rangeExponentialyDistributedRandomSequence(final int range) {
        return rangeExponentialyDistributedRandomSequence(0, range);
    }

    public static double[] rangeUniformlyDistributedRandomSequence(final int seed, final int
            minValue, final int maxValue) {
        double[] prevSequence = standardUniformlyDistributedRandomSequence(seed);
        double[] sequence = new double[prevSequence.length];
        int range = maxValue - minValue;
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = minValue + range * prevSequence[i];
        }
        return sequence;
    }

    public static double[] rangeUniformlyDistributedRandomSequence(final int
                                                                           minValue, final int maxValue) {
        return rangeUniformlyDistributedRandomSequence(SEED_1, minValue, maxValue);
    }

    @SuppressWarnings("unused")
    public static double[] rangeUniformlyDistributedRandomSequence(final int range) {
        return rangeUniformlyDistributedRandomSequence(0, range);
    }

    public static double mean(double[] sequence) {
        double sum = 0;
        for (double num: sequence) {
            sum += num;
        }
        return sum/sequence.length;
    }

    public static double mean(int[] sequence) {
        int sum = 0;
        for (int num: sequence) {
            sum += num;
        }
        return sum/sequence.length;
    }

    public static double variance(double[] sequence) {
        double mean = mean(sequence);
        double sum = 0;
        for (double num: sequence) {
            sum += pow(num - mean, 2);
        }
        return sum/(sequence.length - 1);
    }

    public static double standardDeviation(double[] sequence) {
        return sqrt(variance(sequence));
    }

    public static double[] covariance(double[] prevSequence) {
        final int n = prevSequence.length;
        final double mean = mean(prevSequence);
        double[] sequence = new double[n - 1];
        for (int j = 1; j <= (n - 1); j++) {
            double sum = 0;
            for (int i = 0; i < n - j; i++) {
                sum += (prevSequence[i] - mean) * (prevSequence[i + j] - mean);
            }
            sequence[j - 1] = sum / (n - j);
        }
        return sequence;
    }

    public static double[] correlation(double[] prevSequence) {
        double variance = variance(prevSequence);
        double[] covariance = covariance(prevSequence);
        double[] sequence = new double[covariance.length];
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = covariance[i] / variance;
        }
        return sequence;
    }

    public static double meanSquaredDisplacement (double[] sequence) {
        return sqrt(variance(sequence) / sequence.length);
    }

    public static double accuracy(double[] sequence) {
        return meanSquaredDisplacement(sequence) * u;
    }

    public static double[] confidenceInterval(double[] sequence) {
        final double mean = mean(sequence);
        final double accuracy = accuracy(sequence);
        return new double[]{mean - accuracy, mean + accuracy};
    }

    public static double exponentialDistributionDensityAtPoint(double x, double mean) {
        return (1 / mean) * Math.pow(Math.E, (-x / mean));
    }

    @SuppressWarnings("unused")
    public static String sequenceToString(double[] sequence) {
        StringBuilder builder = new StringBuilder();
        for (double num: sequence) {
            builder.append(num);
            builder.append(" ");
        }
        return builder.toString();
    }

}
