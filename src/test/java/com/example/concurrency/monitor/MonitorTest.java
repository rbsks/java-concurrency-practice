package com.example.concurrency.monitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class MonitorTest {

    /**
     * <p>bankAccount1TransferToBankAccount2 스레드에서 bankAccount1 -> bankAccount2 송금을 하기위해 bankAccount1 계좌의 모니터 락을 획득
     * <p>bankAccount2TransferToBankAccount1 스레드에서 bankAccount2 -> bankAccount1 송금을 하기위해 bankAccount2 계좌의 모니터 락을 획득
     * <p>이러한 상황은 transfer 메서드에서 데드락이 발생할 수 있다.
     *
     * @see com.example.concurrency.monitor.BankAccount#transfer(BankAccount to, double amount)
     */
    @Test
    public void transferTest() {
        BankAccount bankAccount1 = new BankAccount(10000);
        BankAccount bankAccount2 = new BankAccount(10000);

        Thread bankAccount1TransferToBankAccount2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                boolean transfer = bankAccount1.transfer(bankAccount2, 10);
                if (transfer) {
                    log.info("bankAccount1 -> bankAccount2 계좌이체 성공");
                } else {
                    log.info("bankAccount1 -> bankAccount2 계좌이체 실패");
                }
            }
        });

        Thread bankAccount2TransferToBankAccount1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                boolean transfer = bankAccount2.transfer(bankAccount1, 10);
                if (transfer) {
                    log.info("bankAccount2 -> bankAccount1 계좌이체 성공");
                } else {
                    log.info("bankAccount2 -> bankAccount1 계좌이체 실패");
                }
            }
        });

        bankAccount1TransferToBankAccount2.start();
        bankAccount2TransferToBankAccount1.start();

        try {
            bankAccount1TransferToBankAccount2.join();
            bankAccount2TransferToBankAccount1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("bankAccount1 balance: {}", bankAccount1.getBalance());
        log.info("bankAccount2 balance: {}", bankAccount2.getBalance());
    }

    @Test
    public void produceAndConsumeTest() {
        ShareQueue queue = new ShareQueue();

        Thread produce = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                queue.produce(i);
            }
        }, "produce");

        Thread consume = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                queue.consume();
            }
        }, "consume");

        produce.start();
        consume.start();

        try {
            produce.join();
            consume.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
