package com.example.concurrency.exam3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class TerminatedStateThreadTest {

    @Test
    @DisplayName("Thread가 Terminated인 상태")
    public void terminatedStateThread() throws Exception {
        //given
        Thread thread = new Thread(() -> log.info("thread started"));

        //when
        thread.start();
        thread.join();
        log.info("thread state: {}", thread.getState());
    }
}
