package com.example.concurrency.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockTest {

    @Test
    public void reentrantLockTest() {
        CountLock countLock = new CountLock();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                countLock.increment();
            }
        }, "Thread-1");
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                countLock.increment();
            }
        }, "Thread-1");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("count: {}", countLock.getCount());
    }

    @Test
    public void lockAndUnlockTest() {
        Lock lock = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            lock.lock();
            try {
                log.info("{} lock-1", threadName);
                lock.tryLock();
                try {
                    log.info("{} lock-2", threadName);
                } finally {
                    log.info("{} unlock-2", threadName);
                    lock.unlock();
                }
            } finally {
                log.info("{} unlock-1", threadName);
                lock.unlock();
            }
        });

        Thread thread2 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            lock.lock();
            try {
                log.info("{} lock-1", threadName);
            } finally {
                log.info("{} unlock-1", threadName);
                lock.unlock();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
