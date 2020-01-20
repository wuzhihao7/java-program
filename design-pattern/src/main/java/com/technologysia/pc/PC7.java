package com.technologysia.pc;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

public class PC7 {
    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new LinkedTransferQueue<>();
        DataProducer dataProducer = new DataProducer(blockingQueue);
        new Thread(dataProducer).start();
        for (int i = 0; i < 4; i++) {
            DataConsumer dataConsumer = new DataConsumer(blockingQueue);
            dataConsumer.addObserver(dataProducer);
            new Thread(dataConsumer).start();
        }
    }
}

class DataProducer implements Observer, Runnable {

    private BlockingQueue<String> blockingQueue;

    private boolean handling = true;

    public DataProducer(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        while (handling) {
            try
            {
                String str =  handleData();
                blockingQueue.offer(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("生产者停止");
    }

    private String handleData() {
        return "str";
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.toString().equals("stopHandling")) {
            System.out.println("stopHandling data");
            handling = false;
            blockingQueue.clear();
//            blockingQueue.offer("str");
        }
    }
}

class DataConsumer extends Observable implements Runnable {

    private BlockingQueue<String> blockingQueue;

    public DataConsumer(BlockingQueue<String> blockingDeque) {
        this.blockingQueue = blockingDeque;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String str = blockingQueue.take();
                handleData(str);
            } catch (Exception e) {
                notifyObservers("stopHandling");
                break;
            }
        }
    }

    private void handleData(String str) {
        int i = 1/0;
    }

    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }
}
