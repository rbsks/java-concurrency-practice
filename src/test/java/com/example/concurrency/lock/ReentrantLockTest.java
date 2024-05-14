package com.example.concurrency.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockTest {

    @Test
    public void reentrantLockTest() {
        CountLock countLock = new CountLock();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                countLock.increment();
            }
        }, "Thread-1");
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                countLock.increment();
            }
        }, "Thread-1");

        startAndJoinThread(thread1, thread2);

        log.info("count: {}", countLock.getCount());
    }

    @Test
    public void lockAndUnlockTest() {
        Lock lock = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            lock.lock();
            try {
                log.info("{} lock-1", threadName);
                lock.tryLock();
                try {
                    log.info("{} lock-2", threadName);
                } finally {
                    log.info("{} unlock-2", threadName);
                    lock.unlock();
                }
            } finally {
                log.info("{} unlock-1", threadName);
                lock.unlock();
            }
        });

        Thread thread2 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            lock.lock();
            try {
                log.info("{} lock-1", threadName);
            } finally {
                log.info("{} unlock-1", threadName);
                lock.unlock();
            }
        });

        startAndJoinThread(thread1, thread2);
    }

    /**
     * tryLock() 메서드는 락 획득 실패 시 스레드가 차단 또는 대기 상태로 빠지지 않고 락 획득 결과에 따라 별도의 처리를 하고 싶은 경우 사용한다.
     */
    @Test
    public void tryLockNotParameterTest() {
        ReentrantLock lock = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            boolean acquired = false;
            while (!acquired) {
                acquired = lock.tryLock();
                if (acquired) {
                    try {
                        log.info("{} acquired lock", threadName);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        lock.unlock();
                        log.info("{} unlock", threadName);
                    }
                } else {
                    log.info("{} not acquired lock", threadName);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            boolean acquired = false;
            while (!acquired) {
                acquired = lock.tryLock();
                if (acquired) {
                    try {
                        log.info("{} acquired lock", threadName);
                    } finally {
                        lock.unlock();
                        log.info("{} unlock", threadName);
                    }
                } else {
                    log.info("{} not acquired lock", threadName);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "Thread-2");

        startAndJoinThread(thread1, thread2);
    }

    /**
     * <p>tryLock(long timeout, TimeUnit unit) 메서드는 timeout 시간 동안 락을 획득하기 위해 스레드가 대기 한다.
     * <p>아래 테스트에서 Thread-1이 락 획득 후 3초 동안 sleep() 메서드를 호출하여 대기 상태로 빠지기 때문에 결국에는 Thread-2는 락을 획득하지 못 한다.
     */
    @Test
    public void tryLockWithParameterTest() {
        ReentrantLock lock = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            try {
                if (lock.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        log.info("{} acquired lock", threadName);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        lock.unlock();
                        log.info("{} unlock", threadName);
                    }
                } else {
                    log.info("{} not acquired lock", threadName);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            try {
                if (lock.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        log.info("{} acquired lock", threadName);
                    } finally {
                        lock.unlock();
                        log.info("{} unlock", threadName);
                    }
                } else {
                    log.info("{} not acquired lock", threadName);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Thread-2");

        startAndJoinThread(thread1, thread2);
    }

    private void startAndJoinThread(Thread... threads) {
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
