package com.example.concurrency.semaphore;

import org.junit.jupiter.api.Test;

public class CountingSemaphoreTest {

    @Test
    public void countingSemaphoreTest() throws InterruptedException {

        int permits = 10;
        ShareResource shareResource = new ShareResource(new CountingSemaphore(permits));

        int threadCount = 15;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(shareResource::sum);
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
    }
}
