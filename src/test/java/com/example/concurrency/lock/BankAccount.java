package com.example.concurrency.lock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccount {

    private final String name;
    private Transaction transaction;
    private final List<Transaction> transactions;
    private final ReadWriteLock lock;

    public BankAccount(String name) {
        this.name = name;
        this.lock = new ReentrantReadWriteLock();
        this.transactions = new ArrayList<>();
        this.transaction = new Transaction(new BigDecimal("1000"), TransactionType.DEPOSIT);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        Lock readLock = this.lock.readLock();
        try {
            readLock.lock();
            Thread.sleep(500);
            return this.transaction.balance();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
        }
    }

    public List<Transaction> getTransactions() {
        Lock readLock = this.lock.readLock();
        try {
            readLock.lock();
            Thread.sleep(500);
            return this.transactions;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
        }
    }

    public void deposit(BigDecimal amount) {
        Lock writeLock =  this.lock.writeLock();
        try {
            writeLock.lock();
            Thread.sleep(1000);
            BigDecimal balance = this.transaction.balance();
            this.transaction = new Transaction(balance.add(amount), TransactionType.DEPOSIT);
            this.transactions.add(new Transaction(amount, TransactionType.DEPOSIT));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    public void withdrawal(BigDecimal amount) {
        Lock writeLock =  this.lock.writeLock();
        try {
            writeLock.lock();
            BigDecimal balance = transaction.balance();
            if (balance.compareTo(amount) < 0) {
                throw new RuntimeException("잔액 부족으로 인해 출금이 불가능 합니다.");
            }

            Thread.sleep(1000);
            this.transaction = new Transaction(balance.subtract(amount), TransactionType.WITHDRAWAL);
            this.transactions.add(new Transaction(amount, TransactionType.WITHDRAWAL));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }
}
