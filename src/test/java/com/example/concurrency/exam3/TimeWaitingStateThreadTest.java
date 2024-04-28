package com.example.concurrency.exam3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class TimeWaitingStateThreadTest {

    @Test
    @DisplayName("Thread가 Time Wait인 상태")
    public void timeWaitStateThread() throws Exception {
        //given
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        //when
        thread.start();
        Thread.sleep(100);
        log.info("thread state: {}", thread.getState());
    }
}
