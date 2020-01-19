package com.technologysia.pc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PC5 {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue storage = new ArrayBlockingQueue(100000);

        Producer5 producer = new Producer5(storage);
        Thread producerThread = new Thread(producer);
        producerThread.start();
//        Thread.sleep(1000);//1s足够让生产者把阻塞队列塞满

        Consumer5 consumer = new Consumer5(storage);
        while(consumer.needMoreNums()){
            System.out.println(storage.take() + "被消费");
            Thread.sleep(100);//让消费者消费慢一点，给生产者生产的时间
        }

        System.out.println("消费者消费完毕");
        producerThread.interrupt();
    }
}
class Producer5 implements Runnable{

    private BlockingQueue storage;

    public Producer5(BlockingQueue storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        int num = 0;
        try{
            while(num < 1000000 / 2 && !Thread.currentThread().isInterrupted()){
                if(num % 10 == 0){
                    this.storage.put(num);
                    System.out.println(num + "是100的倍数，已经被放入仓库");
                }
                num++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("生产者停止生产");
        }
    }
}

class Consumer5{
    private BlockingQueue storage;

    public Consumer5(BlockingQueue storage) {
        this.storage = storage;
    }

    public boolean needMoreNums(){
        return Math.random() < 0.95 ? true : false;
    }
}