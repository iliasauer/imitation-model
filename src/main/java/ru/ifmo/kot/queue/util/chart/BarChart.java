package ru.ifmo.kot.queue.util.chart;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class BarChart extends Chart {

    public List<Object> intervalsLabels;

    public BarChart(String name, List<Object> intervalsLabels, List<Object> values) {
        super(name, values);
        this.intervalsLabels = intervalsLabels;
    }

}
