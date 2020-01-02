package com.technologysia.queue;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Coordinator {
    public static final Object POISON_PILL = new Object();//special object to kill consumers
    private int productCount = 3;
    private int consumerCount = 5;

    public void startAll() throws Exception {
        BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(20);
        CountDownLatch noMoreToProduce = new CountDownLatch(productCount);
        //start consumers;
        for (int i = 0; i < consumerCount; i++) {
            new Thread(new Consumer("consumer " + i, queue)).start();
        }
        //start producers;
        for (int i = 0; i < productCount; i++) {
            new Thread(new Producer("producer " + i, queue, noMoreToProduce)).start();
        }
        //wait until all producer down
//        noMoreToProduce.await();
        System.out.println("All producer finished, putting POISON_PILL to the queue to stop consumers!");
        //put poison pill
        queue.put(POISON_PILL);
    }
}