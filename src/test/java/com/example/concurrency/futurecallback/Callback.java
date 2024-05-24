package com.example.concurrency.futurecallback;

public interface Callback<T extends Number> {

   void onComplete(T result);
}
