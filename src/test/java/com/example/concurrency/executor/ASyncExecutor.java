package com.example.concurrency.executor;

import java.util.concurrent.Executor;

public class ASyncExecutor implements Executor {

    /**
     * 새로운 스레드를 생성하여 작업 실행
     * @param command the runnable task
     */
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
