package com.example.concurrency.threadrunnable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExtendsThreadTest {

    @Test
    @DisplayName("Thread class를 extends하여 스레드를 생성하는 예제")
    public void extendsThread() throws Exception {
        //given
        ExamThread examThread = new ExamThread();

        //when
        examThread.start();
    }
}

@Slf4j
class ExamThread extends Thread {

    @Override
    public void run() {
        log.info("thread start: {}", Thread.currentThread().getName());
    }
}
