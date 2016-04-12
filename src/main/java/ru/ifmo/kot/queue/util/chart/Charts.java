package ru.ifmo.kot.queue.util.chart;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.ifmo.kot.queue.util.random.ComplexRandom.*;
import static ru.ifmo.kot.queue.util.random.RandomAuditor.*;

public class Charts {

    private static int n1 = 1000;
    private static int n2 = 100000;

    private static final double[] EXP_SEQUENCE = exponentialyDistributedRandomSequence(SEED_1, a,
            m, n1);
    private static final double[] EXP_SEQUENCE_2 = exponentialyDistributedRandomSequence(SEED_1, a,
            m, n2);
    private static final double[] INTERVALS = equalIntervalsWithEmpiricRule(0, 1, n2);

    private static List<Object> valuesAsList(double[] values) {
        Object[] objArr = ArrayUtils.toObject(values);
        return Arrays.asList(objArr);
    }

    private static double exponentialDistributionDensityAtPoint(double x, double mean) {
        return (1 / mean) * Math.pow(Math.E, (-x / mean));
    }

    public static Chart correlationChart() {
        return indexChart("correlationChart", correlation(EXP_SEQUENCE), 20);
    }

    public static BarChart distributionBarChart() {
        double[] intervalsLabels = new double[INTERVALS.length - 1];
        System.arraycopy(INTERVALS, 1, intervalsLabels, 0, intervalsLabels.length);
        double[] heights = heightsOfBarRectangles(EXP_SEQUENCE_2, INTERVALS);
        return new BarChart("barChart", valuesAsList(intervalsLabels), valuesAsList(heights));
    }

    public static Chart exponentialDistributionDensity() {
        double[] intervalsNext = new double[INTERVALS.length - 1];
        System.arraycopy(INTERVALS, 1, intervalsNext, 0, intervalsNext.length);
        double[] density = new double[intervalsNext.length];
        for (int i = 0; i < intervalsNext.length; i++) {
            density[i] = exponentialDistributionDensityAtPoint(intervalsNext[i], 0.5);
        }
        return new Chart("expDensity", valuesAsList(density));
    }

    public static Chart prevNextChart() {
        return nextElementChart("prevNextChart", EXP_SEQUENCE);
    }

    private static Chart indexChart(String name, double[] sequence, final int numberOfValues) {
        List<Object> chart = new ArrayList<>();
        for (int i = 0; i < numberOfValues; i++) {
            chart.add(new Point(i, sequence[i]));
        }
        return new Chart(name, chart);
    }

    public static Chart indexDoubleChart(String name, List<Double> values) {
        List<Object> chart = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            chart.add(new Point(i, values.get(i)));
        }
        return new Chart(name, chart);
    }

    public static Chart indexIntegerChart(String name, List<Integer> values) {
        List<Object> chart = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            chart.add(new Point(i, values.get(i)));
        }
        return new Chart(name, chart);
    }

    private static Chart nextElementChart(String name, double[] sequence) {
        List<Object> chart = new ArrayList<>();
        for (int i = 0; i < sequence.length - 1; i++) {
            chart.add(new Point(sequence[i], sequence[i + 1]));
        }
        return new Chart(name, chart);
    }

}
