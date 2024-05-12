package com.example.concurrency.vola;

public class Calculator {

    private int count;
    private volatile boolean stop;

    public void increment() {
        count++;
    }

    public void stopFlag() {
        this.stop = true;
    }

    public int getCount() {
        return this.count;
    }

    public boolean getStop() {
        return this.stop;
    }
}
