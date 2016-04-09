package ru.ifmo.kot.queue.util.chart;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.ifmo.kot.queue.util.random.ComplexRandom.*;
import static ru.ifmo.kot.queue.util.random.RandomAuditor.*;

public class Charts {

    private static final double[] expSequence = exponentialyDistributedRandomSequence(SEED_1, a,
            m, 1000);
    private static final double[] intervals = equalIntervalsWithEmpiricRule(0, 1, 1000);

    private static List<Object> valuesAsList(double[] values) {
        Object[] objArr = ArrayUtils.toObject(values);
        return Arrays.asList(objArr);
    }

    private static double exponentialDistributionDensityAtPoint(double x) {
        return Math.pow(Math.E, -x);
    }

    public static Chart correlationChart() {
        return indexChart("correlationChart", correlation(expSequence), 20);
    }

    public static BarChart distributionBarChart() {
        double[] intervalsLabels = new double[intervals.length - 1];
        System.arraycopy(intervals, 1, intervalsLabels, 0, intervalsLabels.length);
        double[] heights = heightsOfBarRectangles(expSequence, intervals);
        return new BarChart("barChart", valuesAsList(intervalsLabels), valuesAsList(heights));
    }

    public static Chart exponentialDistributionDensity() {
        double[] intervalsNext = new double[intervals.length - 1];
        System.arraycopy(intervals, 1, intervalsNext, 0, intervalsNext.length);
        double[] density = new double[intervalsNext.length];
        for (int i = 0; i < intervalsNext.length; i++) {
            density[i] = exponentialDistributionDensityAtPoint(intervalsNext[i]);
        }
        return new Chart("expDensity", valuesAsList(density));
    }

    public static Chart prevNextChart() {
        return nextElementChart("prevNextChart", expSequence);
    }

    private static Chart indexChart(String name, double[] sequence, final int numberOfValues) {
        List<Object> chart = new ArrayList<>();
        for (int i = 0; i < numberOfValues; i++) {
            chart.add(new Point(i, sequence[i]));
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
