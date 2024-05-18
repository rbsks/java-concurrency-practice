package com.example.concurrency.atomic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AtomicTest {

    /**
     * 1-2: 이 영역은 원자성을 보장하지 않는 영역이다. 그렇기 때문에 THREAD-1 에서 1, 2 영역을 수행하는 도중 다른 스레드에서 3번 영역을 수행하여 메인 메모리에
     * 새로운 값으로 변경을 한다면 THREAD-1 에서 3번 영역을 수행 시 메인 메모리로 변경하려는 값과 실제 메인 메모리의 값이 다르기 때문에 변경을 하지 못하고 false를 리턴 받게되는 현상이 나타날 수 있다.
     * <p>3: compareAndSet 메서드를 호출하면 내부적으로 네이티브 메서드를 호출하여 CPU 에서 원자적인 연산을 보장해 줌
     * <p> compareAndSet 대신에 incrementAndGet, getAndIncrement 등의 메서드로 대체가 가능하다. 이 메서드는 내부적으로 compareAndSetXXX 네이티브 메서드를 호출한다.
     */
    @Test
    public void casAtomicTest() {
        final int NUM_THREAD = 3;
        final int LOOP_NUMBER = 100_000;
        AtomicInteger atomicInteger = new AtomicInteger();
        Thread[] threads = new Thread[NUM_THREAD];
        for (int i = 0; i < NUM_THREAD; i++) {
            threads[i] = new Thread(() -> {
                int count = 0;
                String threadName = Thread.currentThread().getName();
                while (count < LOOP_NUMBER) {
                    int expectedValue = atomicInteger.get(); // 1
                    int newValue = expectedValue + 1; // 2

                    // 3
                    if (!atomicInteger.compareAndSet(expectedValue, newValue)) {
                        log.info("[{}] expected value and memory value not equal", threadName);
                        continue;
                    }

                    count++;
                } 
            }, "THREAD-" + (i + 1));
        }

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

        log.info("final value: {}", atomicInteger.get());
    }
}
