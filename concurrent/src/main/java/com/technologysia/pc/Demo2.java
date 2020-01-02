package com.technologysia.pc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Demo2 {

    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> count = new HashMap<>();
        ExecutorService service = Executors.newFixedThreadPool(10);
        ProducerConsumer<String> pc = new ProducerConsumer<>();
        //一个线程进行查询
        ProducerConsumer.Producer p = pc.new Producer();
        service.submit(p);
        System.err.println("生产线程正在生产中。。。。。。。。。");
        //是个线程进行修改
        for (int i = 0; i < 10; i++) {
            System.err.println("消费线程" + i + "正在消费中。。。。。。。。。。");
            service.submit(pc.new Consumer(count));
        }
        service.shutdown();
        while (!service.awaitTermination(1, TimeUnit.SECONDS)){
        }
        System.out.println(count);
    }
}

class ProducerConsumer<T>{
    private BlockingQueue<T> queue = new ArrayBlockingQueue<T>(1024);
    public static final String POISON = "";
    class Producer implements Runnable {
        @Override
        public void run() {
            ArrayList<T> wordList = new ArrayList<>(10);
            wordList.add((T) "hello");
            wordList.add((T) "hello");
            wordList.add((T) "hello");
            wordList.add((T) "hello2");
            wordList.add((T) "hello2");
            wordList.add((T) "hello2");
            wordList.add((T) "hello3");
            wordList.add((T) "hello3");
            wordList.add((T) "hello3");
            wordList.add((T) "hello4");
            wordList.add((T) "hello4");
            wordList.add((T) "hello4");
            try {
                for (T word : wordList) {
                    queue.put(word);
                }
                queue.put((T) ProducerConsumer.POISON);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Consumer implements Runnable {
        private Map<T, Integer> count;

        public Consumer(Map<T, Integer> count) {
            this.count = count;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    final T word = queue.take();
                    if (word == ProducerConsumer.POISON) {
                        queue.put(word);
                        break;
                    }
                    synchronized (word) {
                        final Integer integer = count.get(word);
                        if (integer == null) {
                            count.put(word, 1);
                        } else {
                            count.put(word, integer + 1);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
