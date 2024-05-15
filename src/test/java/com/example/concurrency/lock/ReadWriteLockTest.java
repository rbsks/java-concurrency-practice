package com.example.concurrency.lock;

import com.example.concurrency.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ReadWriteLockTest {

    /**
     * 아래 테스트는 READ-1, READ-2 스레드는 동시에 읽기 락을 획득하고 공유 자원을 읽어올 수 있다.
     * 하지만 READ-3 스레드는 WRITE-1 스레드 이후에 실행되기 때문에 읽기 락을 획득 하지 못 하고 WRITE-1의 쓰기 작업이 끝날 때까지 대기하게 된다.
     * WRITE-1 스레드가 락을 확득하고 임계영역에 진입하여 쓰기 작업을 마친 후 락을 해제 해야 READ-3 스레드가 읽기 락을 획득하고 공유 자원에 접근하여 읽기 작업을 수행할 수 있다.
     */
    @Test
    public void readWriteLockTest() {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ShareData shareData = new ShareData();

        Thread readThread1 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            Lock readLock = readWriteLock.readLock();
            readLock.lock();
            try {
                try {
                    log.info("[{}] share data: {}", threadName, shareData.getData());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                readLock.unlock();
            }
        }, "READ-1");

        Thread readThread2 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            Lock readLock = readWriteLock.readLock();
            readLock.lock();
            try {
                try {
                    log.info("[{}] share data: {}", threadName, shareData.getData());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                readLock.unlock();
            }
        }, "READ-2");

        Thread readThread3 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            Lock readLock = readWriteLock.readLock();
            readLock.lock();
            try {
                try {
                    log.info("[{}] share data: {}", threadName, shareData.getData());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                readLock.unlock();
            }
        }, "READ-3");

        Thread writeLock = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            Lock writtenLock = readWriteLock.writeLock();
            writtenLock.lock();
            try {
                try {
                    for (int i = 0; i < 10; i++) {
                        shareData.increment();
                    }
                    log.info("[{}] share data: {}", threadName, shareData.getData());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                writtenLock.unlock();
            }
        }, "WRITE-1");

        ThreadUtil.startAndJoinThread(readThread1, readThread2, writeLock, readThread3);
    }

    /**
     * 쓰기 작업 보다 읽기 작업이 많은 경우에는 ReentrantReadWriteLock 사용을 고려해볼 수 있다.
     */
    @Test
    public void readWriteLockDepositTest() {
        BankAccount bankAccount = new BankAccount("gyubin-account");

        Thread[] readThreads1 = new Thread[10];
        for (int i = 0; i < 10; i++) {
            readThreads1[i] = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                BigDecimal balance = bankAccount.getBalance();
                log.info("[{}] {} balance: {}", threadName, bankAccount.getName(), balance);
            }, String.format("READ-THREAD-%s", i + 1));
        }

        Thread[] readThreads2 = new Thread[5];
        for (int i = 0; i < 5; i++) {
            readThreads2[i] = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                BigDecimal balance = bankAccount.getBalance();
                log.info("[{}] {} balance: {}", threadName, bankAccount.getName(), balance);
            }, String.format("READ-THREAD-%s", i + 11));
        }

        Thread[] writeThreads = new Thread[2];
        for (int i = 0; i < 2; i++) {
            writeThreads[i] = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                BigDecimal amount = new BigDecimal("10000");
                bankAccount.deposit(amount);
                log.info("[{}] {} deposit amount: {}", threadName, bankAccount.getName(), amount);
            }, String.format("WRITE-THREAD-%s", i + 1));
        }

        ThreadUtil.startAndJoinThread(readThreads1);
        ThreadUtil.startAndJoinThread(writeThreads);
        ThreadUtil.startAndJoinThread(readThreads2);

        assertThat(new BigDecimal("21000")).isEqualTo(bankAccount.getBalance());
    }

    @Test
    public void readWriteLockWithdrawalTest() {
        BankAccount bankAccount = new BankAccount("gyubin-account");

        Thread[] readThreads1 = new Thread[10];
        for (int i = 0; i < 10; i++) {
            readThreads1[i] = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                BigDecimal balance = bankAccount.getBalance();
                log.info("[{}] {} balance: {}", threadName, bankAccount.getName(), balance);
            }, String.format("READ-THREAD-%s", i + 1));
        }

        Thread[] readThreads2 = new Thread[5];
        for (int i = 0; i < 5; i++) {
            readThreads2[i] = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                BigDecimal balance = bankAccount.getBalance();
                log.info("[{}] {} balance: {}", threadName, bankAccount.getName(), balance);
            }, String.format("READ-THREAD-%s", i + 11));
        }

        Thread[] writeThreads = new Thread[2];
        for (int i = 0; i < 2; i++) {
            writeThreads[i] = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                BigDecimal amount = new BigDecimal("100");
                bankAccount.withdrawal(amount);
                log.info("[{}] {} deposit amount: {}", threadName, bankAccount.getName(), amount);
            }, String.format("WRITE-THREAD-%s", i + 1));
        }

        ThreadUtil.startAndJoinThread(readThreads1);
        ThreadUtil.startAndJoinThread(writeThreads);
        ThreadUtil.startAndJoinThread(readThreads2);

        assertThat(new BigDecimal("800")).isEqualTo(bankAccount.getBalance());
    }
}
