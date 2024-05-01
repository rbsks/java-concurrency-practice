package com.example.concurrency.interrupt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadInterruptTest {

    @Test
    @DisplayName("sleep으로 TIME_WAITING 상태인 스레드가 interrput되면 RUNNABLE 상태로 변경되고 실행된다.")
    public void test() throws Exception {
        //given
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                Thread currentThread = Thread.currentThread();
                log.info("[{}] state: {}", currentThread.getName(), currentThread.getState());
            }
        });

        //when
        thread.start();
        Thread.sleep(1000);
        log.info("[{}] state: {}", thread.getName(), thread.getState());
        thread.interrupt();
    }
}
