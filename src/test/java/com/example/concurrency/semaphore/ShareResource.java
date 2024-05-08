package com.example.concurrency.semaphore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShareResource {

    private int shareValue = 0;
    private final CommonSemaphore semaphore;

    public ShareResource(CommonSemaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void sum() {
        try {
            semaphore.acquired();
            for (int i = 0; i < 10; i++) {
                shareValue++;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("[{}] sum: {}", Thread.currentThread().getName(), this.shareValue);
        } finally {
            semaphore.release();
        }
    }

}
