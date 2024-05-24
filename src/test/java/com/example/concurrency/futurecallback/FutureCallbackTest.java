package com.example.concurrency.futurecallback;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FutureCallbackTest {

    @Test
    public void futureTest() {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future<Integer> future = executorService.submit(() -> {
            try {
                log.info("[{}] 비동기 작업 수행 중", Thread.currentThread().getName());
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return 10;
        });

        log.info("non-blocking 영역: Callable call() 실행 후 결과가 저장되지 않은 Future 바로 반환");
        try {
            log.info("blocking 영역: 비동기 작업의 결과를 받기 위해 get() 메서드를 호출한 스레드는 blocking 됨");

            /*
             * 1. FutureTask awaitDone() 메서드를 호출하고 여기서 WaitNode를 생성하여 비동기 작업의 완료를 기다리   는 스레드를 관리 한다.
             * WaitNode는 Treiber Stack 자료구조로 원자적인 CAS 연산을 사용하여 병렬 환경에서 안전하게 push, pop 연산을 수행할 수 있다.
             * 2. FutureTask를 blocker로 지정하여 LockSupport.park() 메서드를 호출해 현재 스레드가 대기하게 끔 한다.
             * 3. 비동기 작업이 완료 후 run() 메서드 내부의 set() 메서드를 호출하여 FutureTask outcome 변수에 비동기 작업의 결과를 저장하고
             * state 변수의 상태도 NEW(0) -> COMPLETING(1) -> NORMAL(2) 순으로 변경된다.
             * 마지막으로 finishCompletion() 내부의 LockSupport.unpark() 메서드를 호출하여 현재 대기중인 스레드를 깨워준다.
             */
            Integer number = future.get();
            log.info("비동기 작업 결과: {}", number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdown();
    }

    @Test
    public void callbackTest() {

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            try {
                log.info("[{}] 비동기 작업 수행 중", Thread.currentThread().getName());
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Callback<Integer> callback = new MyCallback();
            callback.onComplete(10);
        });

        try {
            executorService.awaitTermination(6_000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdown();
    }

    @Test
    public void futureCallbackTest() {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        registerCallback(
                executorService,
                () -> {
                    try {
                        log.info("[{}] 비동기 작업 수행 중", Thread.currentThread().getName());
                        Thread.sleep(5_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return 10;
                },
                result -> {
                    log.info("callback method 비동기 작업 수행 중: {}", result);
                    log.info("callback method 비동기 작업 수행 결과: {}", result + 1);
                }
        );

        try {
            executorService.awaitTermination(6_000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdown();
    }

    private void registerCallback(ExecutorService executorService, Callable<Integer> task, Callback<Integer> callback) {
        new Thread(() -> {
            Integer result;
            try {
                Future<Integer> future = executorService.submit(task);
                log.info("non-blocking 영역: Callable call() 실행 후 결과가 저장되지 않은 Future 바로 반환");

                log.info("blocking 영역: 비동기 작업의 결과를 받기 위해 get() 메서드를 호출한 스레드는 blocking 됨");
                result = future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

            callback.onComplete(result);
        }).start();
    }
}
