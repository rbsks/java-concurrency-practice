package com.example.concurrency.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncService {

    /**
     * AsyncExecutionInterceptor invoke 메서드에서 Callable 인스턴스를 생성 후 submit
     */
    @Async(value = "customThreadPool")
    public CompletableFuture<Integer> async() {
        log.info("[{}] async annotation", Thread.currentThread().getName());
        return CompletableFuture.completedFuture(123);
    }
}
