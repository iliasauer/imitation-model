package ru.ifmo.kot.queue.util.chart;

import ru.ifmo.kot.queue.util.random.ComplexRandom;

import java.util.ArrayList;
import java.util.List;

import static ru.ifmo.kot.queue.util.random.ComplexRandom.*;

public class Charts {

    private static final double[] expSequence = exponentialyDistributedRandomSequence(SEED_1, a,
            m, 1000);

    public static Chart correlationChart() {
        return indexChart("correlationChart", correlation(expSequence), 20);
    }

    public static Chart prevNextChart() {
        return nextElementChart("prevNextChart", expSequence);
    }

    private static Chart indexChart(String name, double[] sequence, final int numberOfValues) {
        List<Point> chart = new ArrayList<>();
        for (int i = 0; i < numberOfValues; i++) {
            chart.add(new Point(i, sequence[i]));
        }
        return new Chart(name, chart);
    }

    private static Chart nextElementChart(String name, double[] sequence) {
        List<Point> chart = new ArrayList<>();
        for (int i = 0; i < sequence.length - 1; i++) {
            chart.add(new Point(sequence[i], sequence[i + 1]));
        }
        return new Chart(name, chart);
    }

}
