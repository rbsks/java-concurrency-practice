package com.example.concurrency.monitor;

public class BankAccount {

    private double balance;
    private final Object lock = new Object();

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    private void deposit(double amount) {
        synchronized (this.lock) {
            balance += amount;
        }
    }

    private boolean withdrawal(double amount) {
        synchronized (this.lock) {
            if (balance < amount) {
                return false;
            }

            balance -= amount;
            return true;
        }
    }

    /**
     * 데드락이 발생하는 코드
     *
     * @param to 수취 계좌
     * @param amount 수취 금액
     * @return 송금 성공 여부
     */
    public boolean transfer(BankAccount to, double amount) {
        synchronized (this.lock) {
            if (this.withdrawal(amount)) {
                synchronized (to.lock) {
                    to.deposit(amount);
                    return true;
                }
            }

            return false;
        }
    }

    public double getBalance() {
        return balance;
    }
}
