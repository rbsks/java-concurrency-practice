package com.example.concurrency.join;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ThreadJoinTest {

    @Test
    @DisplayName("메인 스레드는 thread 작업이 완료될 때 까지 기다린다.")
    public void join_01() throws Exception {
        //given
        Thread thread = new Thread(() -> {
            try {
                Thread currentThread = Thread.currentThread();
                log.info("[{}] start", currentThread.getName());
                Thread.sleep(3_000);
                log.info("[{}] end", currentThread.getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

        //when
        thread.start();
        log.info("[{}]가 [{}]의 작업기다린다.", Thread.currentThread().getName(), thread.getName());
        thread.join();
        log.info("[{}] 종료 후 [{}] 동작", thread.getName(), Thread.currentThread().getName());
    }

    @Test
    @DisplayName("메인 스레드는 thread1, thread2 작업이 완료될 때 까지 기다린다.")
    public void join_02() throws Exception {
        //given
        Thread thread1 = new Thread(() -> {
            try {
                Thread currentThread = Thread.currentThread();
                log.info("[{}] start", currentThread.getName());
                Thread.sleep(10_000);
                log.info("[{}] end", currentThread.getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread currentThread = Thread.currentThread();
                log.info("[{}] start", currentThread.getName());
                Thread.sleep(15_000);
                log.info("[{}] end", currentThread.getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        //when
        thread1.start();
        thread2.start();
        log.info("[{}]가 [{}], [{}}의 작업기다린다.", Thread.currentThread().getName(), thread1.getName(), thread2.getName());
        thread1.join();

        // 메인 스레드가 대기 상태로 빠지기 때문에 thread1이 실행이 완료된 후 thread2.join() 메서드를 호출한다
        thread2.join();
        log.info("[{}], [{}} 종료 후 [{}] 동작", thread1.getName(), thread2.getName(), Thread.currentThread().getName());
    }

    @Test
    @DisplayName("mainThread에 interrupt 발생 시 InterruptException이 발생하고 실행 대기(RUNNABLE) 상태로 전환된다.")
    public void join_03() {
        //given
        Thread mainThread = Thread.currentThread();
        Thread longRunningThread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            try {
                log.info("[{}] start", currentThread.getName());
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                mainThread.interrupt();
                log.info("longRunningThread interrupt 발생");
                log.info("[{}] state: {}", currentThread.getName(), currentThread.getState());
            }
        });

        Thread interruptingThread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            try {
                log.info("[{}] start", currentThread.getName());
                Thread.sleep(2_000);
                log.info("[{}]에 interrupt", longRunningThread.getName());
                longRunningThread.interrupt();
            } catch (InterruptedException e) {
                log.info("[{}] state: {}", currentThread.getName(), currentThread.getState());
            }
        });

        //when
        longRunningThread.start();
        interruptingThread.start();

        try {
            log.info("longRunningThread join");
            longRunningThread.join();
        } catch (InterruptedException e) {
            log.info("[{}] state: {}", mainThread.getName(), mainThread.getState());
            log.error("main thread interrupt 발생", e);
            assertThat(mainThread.getState()).isEqualTo(Thread.State.RUNNABLE);
        }
    }
}
