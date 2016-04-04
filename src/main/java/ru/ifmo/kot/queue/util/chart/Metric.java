package ru.ifmo.kot.queue.util.chart;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Metric implements Serializable {

    public String name;
    public List<Point> values;

    public Metric(String name, List<Point> values) {
        this.name = name;
        this.values = values;
    }
}
