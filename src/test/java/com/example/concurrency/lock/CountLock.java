package com.example.concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountLock {

    private int count;
    private final Lock lock;

    public CountLock() {
        this.lock = new ReentrantLock();
    }

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return this.count;
        } finally {
            lock.unlock();
        }
    }
}
