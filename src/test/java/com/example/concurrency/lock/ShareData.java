package com.example.concurrency.lock;

import lombok.Getter;

@Getter
public class ShareData {

    private int data;

    public void increment() {
        this.data++;
    }
}
