package com.example.concurrency.threadstop;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadStopTest {

    private boolean notVolatileRunning = true;
    private volatile boolean volatileRunning = true; // AtomicBoolean으로 대체 가능

    @Test
    @DisplayName("volatile 키워드가 붙지 않으면 cpu cache 영역에서 notVolatileRunning 값을 읽어오기 때문에 스레드가 중지되지 않을 수 있다.")
    public void notVolatileRunningFlagThreadStop() throws Exception {
        //given
        Thread thread1 = new Thread(() -> {
            int count = 0;
            while (notVolatileRunning) {
                count += 1;
            }

            log.info("[{}] count: {} exit", Thread.currentThread().getName(), count);
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("[{}] exit", Thread.currentThread().getName());
            notVolatileRunning = false;
        });

        //when
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    @DisplayName("volatile 키워드가 붙으면 cpu cache 영역에서 volatileRunning 값을 읽어오지 않고 메모리에서 직접 읽기 때문에 스레드가 중지된다.")
    public void volatileRunningFlagThreadStop() throws Exception {
        //given
        Thread thread1 = new Thread(() -> {
            int count = 0;
            while (volatileRunning) {
                count += 1;
            }

            log.info("[{}] count: {} exit", Thread.currentThread().getName(), count);
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("[{}] exit", Thread.currentThread().getName());
            volatileRunning = false;
        });

        //when
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
