package com.keehoo.thread;

import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/13
 */
public class InterruptDemo {
    public static void main(String[] args) {
//        interrupt1();
//        interrupt2();
//        intettupt3();
        interrupt4();
    }

    public static void interrupt1() {
        final Thread sleepThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };

        Thread busyThread = new Thread() {
            @Override
            public void run() {
                while (true) {

                }
            }
        };

        sleepThread.start();
        busyThread.start();
        sleepThread.interrupt();
        busyThread.interrupt();
        while (sleepThread.isInterrupted()) {
        }

        System.out.println("sleepThread isInterrupted: " + sleepThread.isInterrupted());
        System.out.println("busyThread isInterrupted: " + busyThread.isInterrupted());
    }

    /**
     * 中断阻塞状态的线程
     */
    public static void interrupt2() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("进入睡眠状态");
                    Thread.sleep(10000);
                    System.out.println("睡眠完毕");
                } catch (InterruptedException e) {
                    System.out.println("得到中断异常");
                }
                System.out.println("run方法执行完毕");
            }
        };
        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    public static void intettupt3() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                IntStream.range(0, Integer.MAX_VALUE).forEach(i -> {
                    if (!isInterrupted()) {
                        System.out.println(i);
                    } else {
                        System.out.println("线程被中断了");
                        System.exit(1);
                    }
                });
            }
        };
        thread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    public static void interrupt4() {
        MyThread thread = new MyThread();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.isStop(true);

    }
}

class MyThread extends Thread {
    private volatile boolean stop;

    @Override
    public void run() {
        IntStream.range(0, Integer.MAX_VALUE).forEach(i -> {
            if (!stop) {
                System.out.println(i);
            } else {
                System.out.println("线程被设置了停止");
                System.exit(1);
            }
        });
    }

    public void isStop(boolean stop) {
        this.stop = stop;
    }
}
