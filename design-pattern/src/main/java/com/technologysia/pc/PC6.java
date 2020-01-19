package com.technologysia.pc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class PC6 {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> storage = new ArrayBlockingQueue<>(100000);

        Producer6 producer = new Producer6(storage);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        Consumer6 consumer = new Consumer6(storage, producerThread);
        new Thread(consumer).start();
    }
}

class Producer6 implements Runnable {

    private BlockingQueue<Integer> storage;

    public Producer6(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        int num = 0;
        try {
            while (num < 1000000 / 2 && !Thread.currentThread().isInterrupted()) {
                if (num % 10 == 0) {
                    this.storage.put(num);
                    System.out.println(num + "是100的倍数，已经被放入仓库");
                }
                num++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            storage.offer(666666);
            System.out.println("生产者停止生产");
        }
    }
}

class Consumer6 implements Runnable {
    private final BlockingQueue<Integer> storage;
    private final Thread thread;

    public Consumer6(BlockingQueue<Integer> storage, Thread thread) {
        this.storage = storage;
        this.thread = thread;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer take = storage.take();
                if (take.equals(666666)) {
                    break;
                }
                int i = 1/0;
            }
        } catch (Exception e) {
            thread.interrupt();
            e.printStackTrace();
        }
    }
}