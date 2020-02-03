package com.technologysia.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CylicBarrierTest {
    private static CyclicBarrier cyclicBarrier;
    public static class PrepareReadDataThread extends Thread{
        private int id;
        PrepareReadDataThread(int id){
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("线程 " + this.id + " 可以独数据啦！");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("所有任务完毕，继续处理其他任务... " + System.currentTimeMillis());
        }
    }

    public static class PrepareWriteDataThread extends Thread{
        private int id;
        PrepareWriteDataThread(int id){
            this.id = id;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3000L);
                System.out.println("线程 " + this.id + " 可以load写数据完成！");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("所有任务完毕，继续处理其他任务..." + System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {
        cyclicBarrier = new CyclicBarrier(5, () -> System.out.println("测试开始！"));
        for (int i=0; i< 5; i++){
            new PrepareReadDataThread(i).start();
        }

        //重置
        cyclicBarrier.reset();

        for (int i=0; i< 5; i++){
            new PrepareWriteDataThread(i).start();
        }
    }
}
