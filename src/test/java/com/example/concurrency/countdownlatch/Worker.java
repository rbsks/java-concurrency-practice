package com.example.concurrency.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Worker implements Runnable {

    CountDownLatch startSignal;
    CountDownLatch doneSignal;

    public Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        try {
            startSignal.await();
            String threadName = Thread.currentThread().getName();
            log.info("[{}] working", threadName);
            Thread.sleep(1000);
            log.info("[{}] done working", threadName);
            doneSignal.countDown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }
    }
}
