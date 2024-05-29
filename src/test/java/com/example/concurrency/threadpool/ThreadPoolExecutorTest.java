package com.example.concurrency.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPoolExecutorTest {

    @Test
    public void normalThreadPoolExecutorTest() {

        int corePoolSize = 2;
        int maxPoolSize = 4;
        long keepAliveTime = 0L;
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(4);
        int taskNum = 16;
        try (ThreadPoolExecutor threadPoolExecutor =
                     new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);) {
            for (int i = 0; i < taskNum; i++) {
                threadPoolExecutor.execute(() -> {
                    try {
                        log.info("[{}] working start", Thread.currentThread().getName());
                        Thread.sleep(500);
                        log.info("[{}] working end", Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            threadPoolExecutor.shutdown();
        } catch (Exception e) {
            log.error("exception: ", e);
        }
    }

    @Test
    public void preStartAllCoreThreadsPoolExecutorTest() {

        int corePoolSize = 2;
        int maxPoolSize = 4;
        long keepAliveTime = 0L;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(4);
        int taskNum = 8;
        try (ThreadPoolExecutor threadPoolExecutor =
                     new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);) {
            threadPoolExecutor.prestartAllCoreThreads();
            for (int i = 0; i < taskNum; i++) {
                threadPoolExecutor.execute(() -> {
                    try {
                        log.info("[{}] working start", Thread.currentThread().getName());
                        Thread.sleep(500);
                        log.info("[{}] working end", Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            threadPoolExecutor.shutdown();
        } catch (Exception e) {
            log.error("exception: ", e);
        }
    }

    @Test
    public void threadPool() {
        int corePoolSize = 4;
        int maxPoolSize = 4;
        long keepAliveTime = 0L;
         LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        int taskNum = 8;
        try (ThreadPoolExecutor threadPoolExecutor =
                     new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);) {
//            threadPoolExecutor.prestartAllCoreThreads();
            for (int i = 0; i < taskNum; i++) {
                threadPoolExecutor.submit(() -> {
                    try {
                        log.info("[{}] working start", Thread.currentThread().getName());
                        Thread.sleep(500);
                        log.info("[{}] working end", Thread.currentThread().getName());

                        return 100;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            threadPoolExecutor.shutdown();
        } catch (Exception e) {
            log.error("exception: ", e);
        }

    }
}
