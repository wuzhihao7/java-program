package com.technologysia.queue;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private String name;
    private BlockingQueue<Object> queue;

    public Consumer(String name, BlockingQueue<Object> queue){
        this.name = name;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " started.");
            while (true) {
                Object item = queue.take();
                //poison pill processing
                if (item == Coordinator.POISON_PILL) {
                    queue.put(item);//put back to kill others
                    System.out.println(name + " finished");
                    break;
                }
                item = null;//pretend to consume the item;
                System.out.println(name + " consumed one");
            }
        } catch (InterruptedException e) {
        }
    }
}