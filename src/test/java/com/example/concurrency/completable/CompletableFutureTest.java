package com.example.concurrency.completable;

import com.example.concurrency.monitor.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Function;
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

        List<CompletableFuture<List<Member>>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CompletableFuture<List<Member>> future = CompletableFuture.supplyAsync(() -> {
                log.info("[{}] working", Thread.currentThread().getName());
                return memberService.getMembers();
            });

            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        List<Member> result = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
        ).join();

        log.info("size: {}", result.size());
        result.forEach((member) -> log.info("member: {}", member));
    }

    /**
     * thenApply의 sync 동작
     * <p>supplyAsync 메서드를 호출하면 CompletableFuture를 바로 반환하게 된다. 이때 supplyAsync 메서드로 인해 시작된 비동기 작업이
     * 완료되어 CompletableFuture에 작업의 결과가 있는 경우 별도의 스레드에서 비동기적으로 작업을 처리하는 것이 아니라 thenApply를 호출한 스레드에서 동기적으로 작업을 처리한다.
     * 하지만 supplyAsync 메서드를 호출하고 CompletableFuture를 반환하는 과정이 굉장히 빠르기 때문에 비동기 작업이 완료되어 CompletableFuture에 작업의 결과가
     * 담기는 경우가 거의 없다. 그렇기 때문에 아래 예제 처럼 이전 작업을 실행했던 스레드에서 비동기적으로 실행될 가능성이 훨씬 높다.
     *
     * @see java.util.concurrent.CompletableFuture#uniApplyNow(Object, Executor, Function)
     */
    @Test
    public void supplyAsyncThenApply_sync() {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> 1)
                .thenApply((result) -> {
                    log.info("[{}] working test worker thread", Thread.currentThread().getName());
                    return result + 2;
                });

        Integer result = completableFuture.join();
        log.info("result: {}", result);
    }

    /**
     * thenApply의 async 동작
     * <p>이전 작업이 완료되지않아 CompletableFuture에 작업의 결과가 없는 경우 이전에 작업을 수행했던 동일한 ForkJoinPool에서 비동기적으로 작업을 처리
     *
     * @see java.util.concurrent.CompletableFuture#unipush(CompletableFuture.Completion) 
     * @see java.util.concurrent.CompletableFuture.AsyncSupply#run()
     * @see java.util.concurrent.CompletableFuture#postComplete()
     * @see java.util.concurrent.CompletableFuture.UniApply#tryFire(int mode)
     */
    @Test
    public void supplyAsyncThenApply_async() {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("[{}] working fork join pool worker thread", Thread.currentThread().getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 1;
        }).thenApply((result) -> {
            log.info("[{}] working fork join pool worker thread", Thread.currentThread().getName());
            return result + 2;
        });

        Integer result = completableFuture.join();
        log.info("result: {}", result);
    }
}
