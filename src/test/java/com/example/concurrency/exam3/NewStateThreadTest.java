package com.example.concurrency.exam3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class NewStateThreadTest {

    @Test
    @DisplayName("Thread가 New인 상태")
    public void newStateThread() throws Exception {
        //given
        Thread thread = new Thread(() -> log.info("thread not started"));

        //when
        log.info("thread state: {}", thread.getState());
    }
}
