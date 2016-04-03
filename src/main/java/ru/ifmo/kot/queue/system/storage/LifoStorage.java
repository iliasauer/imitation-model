package ru.ifmo.kot.queue.system.storage;

import java.util.concurrent.LinkedBlockingDeque;

class LifoStorage<T> extends LinkedBlockingDeque<T> {

    LifoStorage(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(T job) {
        return super.offerFirst(job);
    }
}
