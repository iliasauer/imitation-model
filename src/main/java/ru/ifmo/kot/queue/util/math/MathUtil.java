package ru.ifmo.kot.queue.util.math;

public class MathUtil {

    public static double round(final double value, final int numberOfDigits) {
        double rounder = 1.0;
        for (int i = 0; i < numberOfDigits; i++) {
            rounder *= 10.0;
        }
        return Math.round(value * rounder) / rounder;
    }

}
