package com.example.concurrency.interrupt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadInterruptTest {

    @Test
    @DisplayName("TIME_WAITING 상태인 스레드에 인터럽트 메서드가 호출 되면 해당 스레드는 RUNNABLE 상태로 변경된다.")
    public void threadInterrupt() throws Exception {
        //given
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                /*
                InterruptException이 발생하면 해당 스레드의 인터럽트 상태는 false로 초기화 됨.
                다른 곳에서 해당 스레드의 인터럽트 상태를 참하고 있다면 예외 구문에서 해당 스레드의 interrupt() 메서드를 호출해서 인터럽트 상태를 true로 바꿔줘야 한다.
                 */
                Thread currentThread = Thread.currentThread();
                log.info("[{}] isInterrupt: {}", currentThread.getName(), currentThread.isInterrupted());
                log.info("[{}] state: {}", currentThread.getName(), currentThread.getState());
            }
        });

        //when
        thread.start();
        Thread.sleep(1000);
        log.info("[{}] state: {}", thread.getName(), thread.getState());
        thread.interrupt(); // 스레드의 인터럽트 상태를 true로 변경.
    }
}
