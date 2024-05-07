package com.example.concurrency.mutex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShareData {

    private int shareValue = 0;
    private final Mutex mutex;

    public ShareData(Mutex mutex) {
        this.mutex = mutex;
    }

    public void sum() {
        try {
            mutex.ac();
            for (int i = 0; i < 10; i++) {
                shareValue++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("[{}] sum: {}", Thread.currentThread().getName(), this.shareValue);
        } finally {
            mutex.re();
        }
    }

}
