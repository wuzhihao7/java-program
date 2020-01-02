package com.technologysia.cyclicbarrier;

import java.util.concurrent.*;

public class CyclicBarrierDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
        for (int i=0; i < 50; i++){
            int finalI = i;
            executorService.execute(() -> {
                try {
                    System.out.println(finalI +"等待其他处理完毕");
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
//                System.out.println(finalI +"处理完毕");
//                try {
//                    Thread.sleep(1000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            });
        }
//        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("线程还在执行。。。");
        }
    }
}
