package com.example.concurrency.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncService {

    private final Executor customThreadPool;

    /**
     * AsyncExecutionInterceptor invoke 메서드에서 Callable 인스턴스를 생성 후 submit
     */
    @Async(value = "customThreadPool")
    public CompletableFuture<Integer> async() {
        return CompletableFuture.supplyAsync(() -> {
            log.info("[{}] async annotation", Thread.currentThread().getName());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 123;
        }, customThreadPool);
    }
}

