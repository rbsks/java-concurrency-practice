package com.example.concurrency.mutex;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class MutexTest {

    @Test
    public void mutex() throws InterruptedException {
        ShareData shareData = new ShareData(new Mutex());

        Thread thread1 = new Thread(shareData::sum);
        Thread thread2 = new Thread(shareData::sum);
        Thread thread3 = new Thread(shareData::sum);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }
}
