package com.example.concurrency.threadstate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class BlockedStateThreadTest {

    @Test
    @DisplayName("Thread가 Blocked인 상태")
    public void blockedStateThread() throws Exception {
        //given
        Object lock = new Object();
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
               while (true) {

               }
            }
        });

        Thread thread2 = new Thread(() -> {
            log.info("임계 영역에 접근하기 위해 lock 획득 시도");
            synchronized (lock) {
                // 이 로그는 콘솔에 찍히지 않음
                log.info("lock을 획득하기 위해 대기");
            }
        });

        //when
        thread1.start();
        Thread.sleep(100);
        thread2.start();
        log.info("thread1 state: {}", thread1.getState());
        log.info("thread2 state: {}", thread2.getState());
    }
}
