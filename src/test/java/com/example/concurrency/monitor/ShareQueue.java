package com.example.concurrency.monitor;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Queue;

@Slf4j
public class ShareQueue {

    private Queue<Integer> queue = new ArrayDeque<>();
    private final Object lock = new Object();
    private final int CAPACITY = 5;

    public void produce(int item) {
        synchronized (lock) {
            while (queue.size() == CAPACITY) {
                log.info("queue is full");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            queue.add(item);
            log.info("produce: {}", item);
            lock.notifyAll();
        }
    }

    public void consume() {
        synchronized (lock) {
            while (queue.isEmpty()) {
                log.info("queue is empty");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            Integer item = queue.poll();
            log.info("consume: {}", item);
            lock.notifyAll();
        }
    }
}
