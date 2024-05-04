package com.example.concurrency.uncaughtexception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * 스레드에서 발생한 예외를 throw 하여 처리하고 싶은 경우에는
 * UncaughtExceptionHandler가 등록되어 있으면 스레드 내부에서 예외 발생 시 JVM이 dispatchUncaughtException 메서드를 호출하여 핸들러에 등록된
 * 예외 로직이 실행된다. 만약 UncaughtExceptionHandler가 등록되어 있지 않은 경우에는 JVM이 ThreadGroup의 uncaughtException 메서드를 호출하여 예외를 처리하게 된다.
 */
@Slf4j
public class UncaughtExceptionTest {

    @Test
    @DisplayName("스레드에서 발생한 예외는 캐치할 수 없다.")
    public void notCatchException() throws Exception {
        //given
        Thread thread1 = new Thread(() -> {
            throw new RuntimeException("예외 발생");
        });

        //when
        Exception exception = catchException(() -> {
            try {
                thread1.start();
            } catch (RuntimeException e) {
                log.error("thread1 예외 발생", e);
            }
        });

        //then
        assertThat(exception).isNotEqualTo(Exception.class);
        assertThat(exception).isNull();
    }

    @Test
    @DisplayName("모든 스레드에 공통으로 사용할 UncaughtExceptionHandler를 등록하여 스레드에서 발생한 예외를 처리할 수 있다.")
    public void defaultUncaughtException() throws Exception {
        //given
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            log.error("setDefaultUncaughtExceptionHandler [{}]에서 예외 발생 {}", thread.getName(), throwable.getMessage(), throwable);
        });

        Thread thread1 = new Thread(() -> {
            throw new RuntimeException("exception");
        });

        //when
        thread1.start();
    }

    @Test
    @DisplayName("대상 스레드에서 사용 할 UncaughtExceptionHandler를 등록하여 스레드에서 발생한 예외를 처리할 수 있다.")
    public void uncaughtException() throws Exception {
        //given
        Thread thread1 = new Thread(() -> {
            throw new RuntimeException("exception");
        });

        thread1.setUncaughtExceptionHandler((thread, throwable) -> {
            log.error("setUncaughtExceptionHandler [{}]에서 예외 발생 {}", thread.getName(), throwable.getMessage(), throwable);
        });

        //when
        thread1.start();
    }
}
