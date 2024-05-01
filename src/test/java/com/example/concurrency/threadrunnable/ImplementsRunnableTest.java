package com.example.concurrency.threadrunnable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImplementsRunnableTest {

    /**
     * Runnable interface를 구현하는 방식이 Runtime 시점에 Thread 생성자 파라미터에
     * 동적으로 Runnable inteface의 구현체를 바꿀 수 있다는 장점이 있다.
     */
    @Test
    @DisplayName("Runnable interface를 구현하여 스레드를 생성하는 예제")
    public void implementsRunnable() throws Exception {
        //given
        ExamRunnable task = new ExamRunnable();
        Thread examThread = new Thread(task);

        //when
        examThread.start();
    }
}

@Slf4j
class ExamRunnable implements Runnable {

    @Override
    public void run() {
        log.info("thread start: {}", Thread.currentThread().getName());
    }
}
