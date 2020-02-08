package com.technologysia.thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 在子方法中捕获中断异常，但是捕获以后当前线程的中断控制位将被清除，
 * 父方法执行时将无法感知中断。所以此时在子方法中重新设置中断，这样父方法就可以通过对中断控制位的判断来处理中断
 */
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
