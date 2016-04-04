package ru.ifmo.kot.queue.system.storage;

import java.util.concurrent.BlockingQueue;

public class StorageFactory {

    public static Discipline getDiscipline(String disciplineName) {
        for (Discipline discipline: Discipline.values()) {
            if (disciplineName.equalsIgnoreCase(discipline.name())) {
                return discipline;
            }
        }
        return null;
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
