package com.example.concurrency.completable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ForkJoinPoolTest {

    @Test
    public void forkJoinPoolTask() throws Exception {
        int[] array = new int[10];
        for (int i = 0; i < 10; i++) {
            array[i] = i + 1;
        }

        try (ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors())) {
            RecursiveTask<Integer> task = new CustomRecursiveTask(array, 0 , array.length);
            Integer result = forkJoinPool.invoke(task);

            log.info("result: {}", result);
            assertThat(result).isEqualTo(55);
        } catch (Exception e) {
            log.error("exception", e);
        }
    }
}


