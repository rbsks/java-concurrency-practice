package com.example.concurrency.countdownlatch;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class CountDownLatchTest {

    @Test
    public void countDownLatchTest() {
        final int NUM_THREAD = 5;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(5);

        Thread[] threads = new Thread[NUM_THREAD];
        for (int i = 0; i < NUM_THREAD; i++) {
            threads[i] = new Thread(new Worker(startSignal, doneSignal));
        }

        try {
            for (Thread thread : threads) {
                thread.start();
            }

            String threadName = Thread.currentThread().getName();
            log.info("[{}] initialization", threadName);
            Thread.sleep(3000);
            log.info("[{}] done initialization", threadName);
            startSignal.countDown();

            log.info("[{}] wait another thread", threadName);
            doneSignal.await();
            log.info("[{}] exit", threadName);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
