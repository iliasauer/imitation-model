package ru.ifmo.kot.queue.system.storage;

import java.util.concurrent.LinkedBlockingQueue;

public class FifoStorage<T> extends LinkedBlockingQueue<T> {

    public FifoStorage(int capacity) {
        super(capacity);
    }

}
