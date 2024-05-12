package com.example.concurrency.vola;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class VolatileTest {

    @Test
    public void test() {
        Calculator calculator = new Calculator();

        Thread incrementThread = new Thread(() -> {
            while (!calculator.getStop()) {
                calculator.increment();
            }
        });

        Thread readCountThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                calculator.stopFlag();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        incrementThread.start();
        readCountThread.start();

        try {
            incrementThread.join();
            readCountThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
