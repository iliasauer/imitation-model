package ru.ifmo.kot.queue.util.chart;

import ru.ifmo.kot.queue.util.random.ComplexRandom;

import java.util.ArrayList;
import java.util.List;

public class Charts {

    public static Metric correlationChart() {
        return indexChart("correlationChart",
                ComplexRandom.correlation(ComplexRandom.standardUniformlyDistributedRandomSequence()));
    }

    public static Metric prevNextChart() {
        return nextElementChart("prevNextChart",
                ComplexRandom.standardUniformlyDistributedRandomSequence());
    }

    private static Metric indexChart(String name, double[] sequence) {
        List<Point> chart = new ArrayList<>();
        for (int i = 0; i < sequence.length; i++) {
            chart.add(new Point(i, sequence[i]));
        }
        return new Metric(name, chart);
    }

    private static Metric nextElementChart(String name, double[] sequence) {
        List<Point> chart = new ArrayList<>();
        for (int i = 0; i < sequence.length - 1; i++) {
            chart.add(new Point(sequence[i], sequence[i + 1]));
        }
        return new Metric(name, chart);
    }

}
