package com.example.concurrency.threadstate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class RunnableStateThreadTest {

    @Test
    @DisplayName("Thread가 Runnable 상태")
    public void runnableStateThread() throws Exception {
        //given
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1_000_000_000; i++) {
                if (i % 100_000_000 == 0) {
                    log.info("thread state: {}", Thread.currentThread().getState());
                }
            }
        });

        //when
        thread.start();
    }
}
