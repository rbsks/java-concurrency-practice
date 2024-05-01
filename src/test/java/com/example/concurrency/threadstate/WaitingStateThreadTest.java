package com.example.concurrency.threadstate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class WaitingStateThreadTest {

    @Test
    @DisplayName("Thread가 Wait인 상태")
    public void waitStateThread() throws Exception {
        //given
        Object lock = new Object();
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //when
        thread.start();
        Thread.sleep(100);
        log.info("thread state: {}", thread.getState());
    }
}
