package com.example.concurrency.completable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CompletableFutureTest {

    @Test
    public void chainAsyncTask() {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            return 1;
        }).thenApplyAsync(result -> {
            return result + 1;
        });

        try {
            assertThat(completableFuture.get()).isEqualTo(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
