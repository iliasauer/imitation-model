package ru.ifmo.kot.queue.util.random;

public class SimpleRandom {

    private static final java.util.Random RANDOM = new java.util.Random();

    public static int nextInt(int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException();
        }
        int diff = maxValue - minValue;
        return minValue + RANDOM.nextInt(diff + 1);
    }

    public static int nextInt(int range) {
        return 1 + RANDOM.nextInt(range + 1);
    }

}
