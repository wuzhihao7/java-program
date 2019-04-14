package com.keehoo.thread;

import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class YieldDemo {
    class Producer extends Thread {
        @Override
        public void run() {
            IntStream.range(0, 5).forEach(i -> {
                System.out.println("I am Producer: produced item " + i);
            });
        }
    }

    class Consumer extends Thread {
        @Override
        public void run() {
            IntStream.range(0, 5).forEach(i -> {
                System.out.println("I am Consumer: consumed item " + i);
            });
        }
    }

    public static void main(String[] args) {
        YieldDemo demo = new YieldDemo();
        Producer producer = demo.new Producer();
        Consumer consumer = demo.new Consumer();
        producer.setPriority(Thread.MIN_PRIORITY);
        consumer.setPriority(Thread.MAX_PRIORITY);
        producer.start();
        consumer.start();
    }
}
