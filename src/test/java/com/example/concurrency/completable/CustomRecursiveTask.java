package com.example.concurrency.completable;

import java.util.concurrent.RecursiveTask;

public class CustomRecursiveTask extends RecursiveTask<Integer> {

    private final int[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 2;

    public CustomRecursiveTask(int[] array, int start, int end) {

        this.array = array;
        this.start = start;
        this.end = end;
    }

    /**
     * <p> fork는 별도의 ForkJoinWorkerThread가 작업을 분할함.
     * <p> compute는 현재 스레드가 작업을 분할함.
     * <p> ForkJoinPool의 WorkQueue는 ForkJoinWorkerThread를 owner로 가지고 있고 하나의 ForkJoinWorkerThread는 여러 개의 WorkQueue의 owner가 될 수 있다.
     * */
    @Override
    protected Integer compute() {
        if (end - start < THRESHOLD) {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }

            return sum;
        }

        int mid = start + (end - start) / 2;
        CustomRecursiveTask left = new CustomRecursiveTask(array, start, mid);
        CustomRecursiveTask right = new CustomRecursiveTask(array, mid, end);

        left.fork();
        return left.join() + right.compute();
    }
}
