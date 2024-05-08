package com.example.concurrency.mutex;

public class Mutex {

    private boolean lock = false;

    /**
     * wait(), notify() 메서드는 꼭 모니터 락을 획득한 오너 객체에서 호출해야 함 그렇지 않으면 IllegalMonitorStateException 예외 발생
     * 모니터 락을 획득 후 wait() 메서드를 호출하게 되면 모니터 락을 해지하고 Wait Set으로 들어가게된다.
     *
     * 첫 번째 스레드가 모니터 영역에 접근 후 lock 속성을 true로 변경하고 모니터 락을 해지함
     * 두 번째 스레드는 모니터 영역으로 접근 시 lock 속성이 true로 인해 대기 상태로 변경 (Wait Set으로 이동)
     * 첫 번째 스레드가 임계 역역에서 연산 종료 후 release() 매서드를 호출하여 lock 속성을 false로 변경 후 notify() 메서드를 호출하여 Wait Set에서 대기하고 있는 두 번째 스레드를 깨우고 Entry Set으로 이동 시킨다.
     * Entry Set으로 이동한 두 번째 스레드는 OS 스케줄러에 의해서 CPU를 할당 받으면 임계 영역에서 연산 수행
     */
    public synchronized void ac() {
        while (this.lock) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.lock = true;
    }

    public synchronized void re() {
        this.lock = false;
        this.notify();
    }
}
