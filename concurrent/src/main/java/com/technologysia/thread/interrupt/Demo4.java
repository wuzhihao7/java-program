package com.technologysia.thread.interrupt;

import java.util.concurrent.TimeUnit;

public class Demo4 implements Runnable {
    @Override
    public void run() {
        while (true){
            if(Thread.currentThread().isInterrupted()){
                System.out.println("响应中断");
                break;
            }
            throwInterrupt();
        }
    }

    private void throwInterrupt() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //重新设置中断
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Demo4());
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
