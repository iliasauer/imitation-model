package ru.ifmo.kot.queue.util.chart;

import java.io.Serializable;

@SuppressWarnings("WeakerAccess")
public class Point implements Serializable {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
