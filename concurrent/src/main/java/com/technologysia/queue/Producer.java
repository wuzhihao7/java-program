package com.technologysia.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Producer implements Runnable {
    private String name;
    private CountDownLatch noMoreToProduce;
    private BlockingQueue<Object> queue;
    private Random random = new Random();


    public Producer(String name, BlockingQueue<Object> queue, CountDownLatch noMoreToProduce){
        this.name = name;
        this.queue = queue;
        this.noMoreToProduce = noMoreToProduce;
    }

    @Override
    public void run() {
        System.out.println(name + " started.");
        try {
            while (true) {
                Object item = randomProduce();
                if (item == null) {
                    break; //break if no more item
                }
                queue.put(item);
                System.out.println(name + " produced one.");
            }
        } catch (InterruptedException e) {
            //log
        } finally{
            System.out.println(name + " finished.");
            noMoreToProduce.countDown();//count down to signal "I finished."
        }
    }

    private Object randomProduce() {
        if (random.nextBoolean()) {
            return new Object();
        }
        return null;
    }
}