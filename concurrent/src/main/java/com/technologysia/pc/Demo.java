package com.technologysia.pc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Demo {
    private static final Map<String, Integer> counts = new HashMap<>();
    public static final String POISON = "";

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
        Map<String, Integer> count = new HashMap<>();
        final Thread producer = new Thread(new Producer(queue));
        final Thread consumer = new Thread(new Consumer(queue, count));
        final Thread consumer2 = new Thread(new Consumer(queue, count));
        consumer.start();
        consumer2.start();
        producer.start();
        producer.join();
        consumer.join();
        consumer2.join();
        System.out.println(count);
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> queue;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        ArrayList<String> wordList = new ArrayList<>(10);
        wordList.add("hello");
        wordList.add("hello");
        wordList.add("hello");
        wordList.add("hello2");
        wordList.add("hello2");
        wordList.add("hello2");
        wordList.add("hello3");
        wordList.add("hello3");
        wordList.add("hello3");
        wordList.add("hello4");
        wordList.add("hello4");
        wordList.add("hello4");
        try {
            for (String word : wordList) {
                queue.put(word);
            }
            queue.put(Demo.POISON);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<String> queue;
    private Map<String, Integer> count;

    public Consumer(BlockingQueue<String> queue, Map<String, Integer> count) {
        this.queue = queue;
        this.count = count;
    }

    @Override
    public void run() {
        try {
            while (true) {
                final String word = queue.take();
                if (word == Demo.POISON) {
                    queue.put(word);
                    break;
                }
                synchronized (word){
                    final Integer integer = count.get(word);
                    if(integer == null){
                        count.put(word, 1);
                    }else{
                        count.put(word, integer+1);
                    }
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
