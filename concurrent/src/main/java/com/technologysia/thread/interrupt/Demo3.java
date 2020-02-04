package com.technologysia.thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 在子方法中把中断异常上抛给父方法，然后在父方法中处理中断
 */
public class Demo3 implements Runnable {
    @Override
    public void run() {
        try {
            while (true){
                System.out.println("go");
                throwInterrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("检测到中断，保存错误日志");
        }
    }

    private void throwInterrupt() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Demo3());
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
