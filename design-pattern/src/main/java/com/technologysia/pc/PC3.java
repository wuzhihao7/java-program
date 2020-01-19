package com.technologysia.pc;

import java.util.concurrent.*;

public class PC3 {
    public static void main(String[] args) {
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);
        Producer producer = new Producer(blockingQueue);
        Thread thread = new Thread(producer);
        Consumer consumer = new Consumer(blockingQueue,thread);
        thread.start();
        new Thread(consumer).start();
    }
}

class Producer implements Runnable{
    private final BlockingQueue<String> blockingQueue;

    Producer(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        while (true){
            blockingQueue.offer("str");
        }
    }
}

class Consumer implements Runnable{

    private final BlockingQueue<String> blockingQueue;

    Consumer(BlockingQueue<String> blockingQueue, Thread thread) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        while (true){
            try {
                this.blockingQueue.take();
                throw new RuntimeException();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}