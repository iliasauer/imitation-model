package ru.ifmo.kot.queue.system.storage;

import java.util.concurrent.BlockingQueue;

public class StorageFactory {

    public enum Discipline {
        FIFO, LIFO
    }

    public static BlockingQueue<Runnable> createStorage(Discipline discipline, int capacity) {
        switch (discipline) {
            case LIFO:
                return new LifoStorage<>(capacity);
            default:
                return new FifoStorage<>(capacity);
        }
    }

}
