package ru.ifmo.kot.queue.system.storage;

import java.util.concurrent.BlockingQueue;

public class StorageFactory {

    public static BlockingQueue<Runnable> currentStorage;

//    public static int currentStorageSize() {
//        return currentStorage.size();
//    }

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
                currentStorage = new LifoStorage<>(capacity);
                return currentStorage;
            default:
                currentStorage = new FifoStorage<>(capacity);
                return currentStorage;
        }
    }

}
