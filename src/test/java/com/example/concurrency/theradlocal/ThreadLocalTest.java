package com.example.concurrency.theradlocal;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalTest {

    /**
     * Thread를 생성 후 ThreadLocal의 get 메서드를 호출 시 ThreadLocalMap이 null 이면
     * 해당 Thread에서만 사용할 수 있는 ThreadLocalMap을 만든다.
     */
    @Test
    public void threadLocalTest_01() throws Exception {
        //given
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        Thread thread1 = new Thread(() -> {
            // ThreadLocal의 get 메서드 내부에서 Thread.currentThread 메서드를 호출 하여 현재 CPU에서 실행중인 스레드를 가져온다.
            log.info("[{}] not set value: {}", Thread.currentThread().getName(), threadLocal.get());
            threadLocal.set(Thread.currentThread().getName());
            log.info("[{}] set value: {}", Thread.currentThread().getName(), threadLocal.get());
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            log.info("[{}] not set value: {}", Thread.currentThread().getName(), threadLocal.get());
            threadLocal.set(Thread.currentThread().getName());
            log.info("[{}] set value: {}", Thread.currentThread().getName(), threadLocal.get());
        }, "Thread-2");

        //when
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    /**
     * Thread는 instance variable로 ThreadLocalMap을 가지고 있음
     * ThreadLocalMap은 instance variable로 Entry(Key: ThreadLocal<?>, Value: Object) 배열을 가지고 있음
     * ThreadLocal의 threadLocalHashCode와 Entry table의 사이즈를 가지고 Entry table의 인덱스를 구함
     * threadLocalHashCode 이 변수는 정적 변수 AtomicInteger를 사용해서 생성된 해시코드며, Entry를 검색하는 Key로 사용 됨
     * ThreadLocal get -> ThreadLocal Map is null -> initial ThreadLocalMap
     *                 -> ThreadLocal Map is not null -> get Entry -> Entry is null -> map set -> return value
     *                                                             -> Entry is not null -> return value
     */
    @Test
    public void threadLocalTest_02() throws Exception {
        //given
        Thread thread2 = new Thread(() -> {
            Thread thread = Thread.currentThread();
            ThreadLocal<String> threadLocal3 = new ThreadLocal<>();
            log.info("[{}] threadLocal3 not set value: {}", Thread.currentThread().getName(), threadLocal3.get());
            threadLocal3.set(Thread.currentThread().getName() + ":1");
            log.info("[{}] threadLocal3 set value: {}", Thread.currentThread().getName(), threadLocal3.get());

            log.info("[{}] threadLocal3 not set value: {}", Thread.currentThread().getName(), threadLocal3.get());
            threadLocal3.set(Thread.currentThread().getName() + ":2");
            log.info("[{}] threadLocal3 set value: {}", Thread.currentThread().getName(), threadLocal3.get());

            ThreadLocal<String> threadLocal4 = new ThreadLocal<>();
            log.info("[{}] threadLocal4 not set value: {}", Thread.currentThread().getName(), threadLocal4.get());
            threadLocal4.set(Thread.currentThread().getName() + ":3");
            log.info("[{}] threadLocal4 set value: {}", Thread.currentThread().getName(), threadLocal4.get());
            log.info("[{}] threadLocal3 set value: {}", Thread.currentThread().getName(), threadLocal3.get());
        }, "Thread-2");

        //when
        thread2.start();
        thread2.join();
    }
}
