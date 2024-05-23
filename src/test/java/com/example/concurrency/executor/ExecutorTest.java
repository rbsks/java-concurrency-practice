package com.example.concurrency.executor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ExecutorTest {

    @Test
    public void syncExecutorTest() {
        SyncExecutor syncExecutor = new SyncExecutor();
        syncExecutor.execute(() -> {
            String threadName = Thread.currentThread().getName();
            log.info("[{}] sync worker..", threadName);
        });
    }

    @Test
    public void asyncExecutorTest() {
        ASyncExecutor asyncExecutor = new ASyncExecutor();
        asyncExecutor.execute(() -> {
            String threadName = Thread.currentThread().getName();
            log.info("[{}] async worker..", threadName);
        });
    }
}
