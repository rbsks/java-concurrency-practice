package com.example.concurrency.executor;

import java.util.concurrent.Executor;

public class SyncExecutor implements Executor {

    /**
     * 새로운 스레드가 생성되지 않고 현재 실행중인 스레드에서 작업 실행
     * @param command the runnable task
     */
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
