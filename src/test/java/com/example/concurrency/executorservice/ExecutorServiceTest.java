package com.example.concurrency.executorservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class ExecutorServiceTest {

    /**
     * invokeAll() 메서드는 컬렉션으로 받은 Future 비동기 작업들이 안료될 때까지 invokeAll() 메서드를 호출한 스레드는 블록되며 작업의 결과를 Future 리스트로 반환한다.
     * 반환받은 Future 리스트를 순회하면서 get() 메서드를 호출 시 이미 작업의 결과가 저장이 되어있기 때문에 블록되지 않고 작업의 결과를 바로 얻을 수 있다.
     */
    @Test
    public void invokeAllTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<Callable<Integer>> callables = List.of(
                () -> {
                    try {
                        Thread.sleep(100);
                        return 10;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    try {
                        Thread.sleep(100);
                        return 20;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    try {
                        Thread.sleep(100);
                        return 30;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        try {
            List<Future<Integer>> futures = executorService.invokeAll(callables);
            int sum = 0;
            for (Future<Integer> future : futures) {
                sum += future.get();
            }

            log.info("sum: {}", sum);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
