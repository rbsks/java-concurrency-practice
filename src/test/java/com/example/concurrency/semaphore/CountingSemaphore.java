package com.example.concurrency.semaphore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountingSemaphore implements CommonSemaphore {

    private int signal;
    private final int permits;

    public CountingSemaphore(int permits) {
        this.signal = permits;
        this.permits = permits;
    }

    @Override
    public synchronized void acquired() {
        while (this.signal == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.signal--;
        log.info("[{}] 락 획득, 현재 세마포어 값: {}", Thread.currentThread().getName(), signal);
    }

    @Override
    public synchronized void release() {
        if (this.signal < permits) {
            this.signal++;
            log.info("[{}] 락 해제, 현재 세마포어 값: {}", Thread.currentThread().getName(), signal);
            this.notify();
        }
    }
}
