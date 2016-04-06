package ru.ifmo.kot.queue.util.chart;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Chart implements Serializable {

    public String name;
    public List<Object> values;

    public Chart(String name, List<Object> values) {
        this.name = name;
        this.values = values;
    }
}
