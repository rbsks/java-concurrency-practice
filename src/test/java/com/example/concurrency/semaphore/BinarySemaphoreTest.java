package com.example.concurrency.semaphore;

import org.junit.jupiter.api.Test;

public class BinarySemaphoreTest {

    @Test
    public void binarySemaphoreTest() throws InterruptedException {

        ShareResource shareResource = new ShareResource(new BinarySemaphore());

        Thread thread1 = new Thread(shareResource::sum);
        Thread thread2 = new Thread(shareResource::sum);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
