package com.keehoo.future;

import java.util.concurrent.*;

public class FutureDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Task task = new Task();
        Future<Integer> future = executorService.submit(task);
        executorService.shutdown();

        Thread.sleep(1000);

        System.out.println("主线程正在执行");
        try {
            System.out.println("task运行结果:" + future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}
class Task implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        System.out.println("子线程正在计算");
        Thread.sleep(3000);
        int sum = 0;
        for(int i=0; i<100; i++){
            sum += i;
        }
        return sum;
    }
}