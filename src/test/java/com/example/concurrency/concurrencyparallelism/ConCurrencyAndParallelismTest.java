package com.example.concurrency.concurrencyparallelism;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConCurrencyAndParallelismTest {

    @Test
    @DisplayName("concurrency & parallelism - cpu core가 여러 작업을 처리")
    public void conCurrencyAndParallelismTest() throws Exception {
        //given
        int processors = Runtime.getRuntime().availableProcessors() * 3;

        List<Integer> tasks = new ArrayList<>();
        for (int i = 0; i < processors; i++) {
            tasks.add(i);
        }

        long startTime = System.currentTimeMillis();

        //when
        int sum = tasks.parallelStream()
                .mapToInt(task -> {
                    try {
                        Thread.sleep(500);
                        return task * task;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sum();

        //then
        long endTime = System.currentTimeMillis();
        log.info("cpu core: {}", processors);
        log.info("작업을 처리하는 데 걸린 시간: {}ms", (endTime - startTime));
    }
}
