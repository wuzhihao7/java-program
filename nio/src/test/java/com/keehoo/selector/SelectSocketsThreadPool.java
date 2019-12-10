package com.technologysia.selector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/12
 */
public class SelectSocketsThreadPool extends SelectSockets {
    private static final int MAX_THREADS = 5;
    private ThreadPool threadPool = new ThreadPool(MAX_THREADS);

    public static void main(String[] args) throws IOException {
        new SelectSocketsThreadPool().go(args);
    }

    @Override
    protected void readDataFromSocket(SelectionKey selectionKey) throws IOException {
        WorkerThread worker = threadPool.getWorker();
        if(worker == null){
            //No threads available. Do nothing. The selection
            // loop will keep calling this method until a
            // thread becomes available. This design could
            // be improved.
            return;
        }
        //Invoking this wakes up the worker thread, then returns
        worker.serviceChannel(selectionKey);
    }

    /**
     * 一个简单的线程池类
     */
    private class ThreadPool {
        List<WorkerThread> idle = new LinkedList<>();

        public ThreadPool(int poolSize) {
            IntStream.rangeClosed(1, poolSize).forEach(i -> {
                WorkerThread thread = new WorkerThread(this);
                thread.setName("Wroker" + i);
                thread.start();
                idle.add(thread);
            });
        }

        /**
         * 找到一个空闲的工作线程
         *
         * @return
         */
        WorkerThread getWorker() {
            WorkerThread workerThread = null;
            synchronized (idle) {
                if (!idle.isEmpty()) {
                    workerThread = idle.remove(0);
                }
            }
            return workerThread;
        }

        /**
         * 把空闲的工作线程放回线程池
         *
         * @param worker
         */
        void returnWorker(WorkerThread worker) {
            synchronized (idle) {
                idle.add(worker);
            }
        }
    }

    /**
     * 一个工作线程类，它可以排出（排空）通道并回显（回显）输入。
     * 每个实例都使用对拥有线程池对象的引用（参考）构造。
     * 启动时，线程永远循环等待唤醒以服务与SelectionKey对象关联的通道。
     * 通过使用SelectionKey对象调用serviceChannel（）方法来确定worker的任务。
     * serviceChannel（）方法将密钥引用存储在线程对象中，然后调用notify（）将其唤醒。
     * 当通道耗尽时，工作线程将自身返回到其父池。
     */
    private class WorkerThread extends Thread {
        private ByteBuffer buffer = ByteBuffer.allocate(1024);
        private ThreadPool pool;
        private SelectionKey key;

        public WorkerThread(ThreadPool pool) {
            this.pool = pool;
        }

        @Override
        public synchronized void run() {
            System.out.println(this.getName() + " is ready.");
            while (true) {
                try {
                    //休眠且释放掉锁对象
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //清除线程中断状态
                    this.interrupt();
                }
                if (key == null) {
                    continue;
                }
                System.out.println(this.getName() + " has been awakened");
                try {
                    drainChannel(key);
                }catch (IOException e){
                    System.out.println("caught '" + e +"' closing channel");
                    //关闭通道并唤醒选择器
                    try {
                        key.channel().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    key.selector().wakeup();
                }
                key = null;
                this.pool.returnWorker(this);
            }
        }

        synchronized void serviceChannel(SelectionKey key) {
            this.key = key;
            key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
            this.notify();
        }

        void drainChannel(SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel) key.channel();
            int count;
            buffer.clear();
            while ((count = channel.read(buffer)) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                buffer.clear();
            }
            if (count == -1) {
                //关闭通道;使key无效
                channel.close();
                return;
            }
            //恢复对OP_READ的兴趣
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
            //循环选择器，使此键再次激活
            key.selector().wakeup();
        }
    }
}
