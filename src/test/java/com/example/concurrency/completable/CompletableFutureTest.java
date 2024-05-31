package com.example.concurrency.completable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    @Test
    public void supplyAsync() {
        MemberService memberService = new MemberService();
        CompletableFuture<List<Member>> listCompletableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[{}] working", Thread.currentThread().getName());
            return memberService.getMembers();
        });

        List<Member> result = listCompletableFuture.join();
        result.forEach((member) -> log.info("member: {}", member));
    }

    @Test
    public void supplyAsyncThenCombine() {
        MemberService memberService = new MemberService();
        CompletableFuture<List<Member>> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[{}] working", Thread.currentThread().getName());

            List<CompletableFuture<List<Member>>> futures = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                CompletableFuture<List<Member>> future = CompletableFuture.supplyAsync(() -> {
                    log.info("[{}] working", Thread.currentThread().getName());
                    return memberService.getMembers();
                });
                futures.add(future);
            }

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

            return allOf.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join)
                            .flatMap(List::stream)
                            .collect(Collectors.toList())
            ).join();
        });

        List<Member> result = completableFuture.join();
        log.info("size: {}", result.size());
        result.forEach((member) -> log.info("member: {}", member));
    }
}
