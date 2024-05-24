package com.example.concurrency.futurecallback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyCallback implements Callback<Integer> {

    @Override
    public void onComplete(Integer result) {
        log.info("callback method 비동기 작업 수행 중: {}", result);
        log.info("callback method 비동기 작업 수행 결과: {}", result + 1);
    }
}
