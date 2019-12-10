package com.technologysia.threadpool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/18
 */
public class SimpleThreadPool extends Thread{
    /**
     * 线程池大小
     */
    private int threadPoolSize;
    /**
     * 最大接受任务
     */
    private int queueSize;
    /**
     * 最小线程
     */
    private int minSize;
    /**
     * 最大线程
     */
    private int maxSize;
    /**
     * 活跃线程
     */
    private int activeSize;
    /**
     * 拒绝策略
     */
    private DiscardPolicy discardPolicy;
    /**
     * 是否被销毁
     */
    private volatile boolean destroy = false;
    /**
     * 默认最小线程树
     */
    private static final int DEFAULT_MIN_THREAD_SIZE = 2;
    /**
     * 活跃线程
     */
    private static final int DEFAULT_ACTIVE_THREAD_SIZE = 5;
    /**
     * 最大线程
     */
    private static final int DEFAULT_MAX_THREAD_SIZE = 10;
    /**
     * 最多执行多少个任务
     */
    private static final int DEFAULT_WORKER_QUEUE_SIZE = 100;
    /**
     * 线程名称前缀
     */
    private static final String THREAD_NAME_PREFIX = "MY-THREAD-NAME-";
    /**
     * 线程组的名称
     */
    private static final String THREAD_POOL_NAME = "SIMPLE-POOL";
    /**
     * 线程组
     */
    private static final ThreadGroup THREAD_GROUP = new ThreadGroup(THREAD_POOL_NAME);
    /**
     * 线程容器
     */
    private static final List<WorkerTask> WORKER_TASKS = new ArrayList<>();
    /**
     * 任务队列容器,也可以用Queue<Runnable> 遵循 FIFO 规则
     */
    private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();
    /**
     * 拒绝策略
     */
    private static final DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("[拒绝执行] - [任务队列溢出...]");
    };

    /**
     * 任务线程状态枚举
     */
    private enum TaskState{
        /**
         * 空闲
         */
        FREE,
        /**
         * 运行中
         */
        RUNNABLE,
        /**
         * 阻塞
         */
        BLOCKED,
        /**
         * 结束
         */
        TERMINATED
    }

    /**
     * 拒绝策略
     */
    static class DiscardException extends RuntimeException{
        DiscardException(String message){
            super(message);
        }
    }

    /**
     * 拒绝策略接口
     */
    private interface DiscardPolicy{
        /**
         * 拒绝
         */
        void discard();
    }

    /**
     * 任务线程具体实现
     */
    private static class WorkerTask extends Thread{
        /**
         * 线程状态
         */
        private TaskState taskState;
        /**
         * 线程编号
         */
        private static int threadInitNumber;

        private static synchronized String nextThreadName(){
            return THREAD_NAME_PREFIX + (++threadInitNumber);
        }

        WorkerTask(){
            super(THREAD_GROUP, nextThreadName());
        }

        @Override
        public void run() {
            Runnable target;
            OUTER:
            while (this.taskState != TaskState.TERMINATED){
                synchronized (TASK_QUEUE){
                    //说明该线程处于空闲状态
                    while (this.taskState == TaskState.FREE && TASK_QUEUE.isEmpty()){
                        try {
                            this.taskState = TaskState.BLOCKED;
                            //没有任务就wait()住，让出cpu执行权
                            TASK_QUEUE.wait();
                        }catch (InterruptedException e){
                            //如果被打断说明当前线程执行了shutdown()方法，线程状态为TERMINATED,直接跳到while便于退出
                            break OUTER;
                        }
                    }
                    target = TASK_QUEUE.removeFirst();
                }
                if(target != null){
                    this.taskState = TaskState.RUNNABLE;
                    //开始任务
                    target.run();
                    this.taskState = TaskState.FREE;
                }
            }
        }

        /**
         * 优雅关闭线程
         */
        void close(){
            this.taskState = TaskState.TERMINATED;
            this.interrupt();
        }
    }

    SimpleThreadPool() {
        this(DEFAULT_MIN_THREAD_SIZE, DEFAULT_ACTIVE_THREAD_SIZE, DEFAULT_MAX_THREAD_SIZE, DEFAULT_WORKER_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    SimpleThreadPool(int minSize, int activeSize, int maxSize, int queueSize, DiscardPolicy discardPolicy){
        this.minSize = minSize;
        this.activeSize = activeSize;
        this.maxSize = maxSize;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;
        initPool();
    }

    /**
     * 初始化操作
     */
    private void initPool() {
        for (int i = 0; i < this.minSize; i++) {
            this.createWorkerTask();
        }
        this.threadPoolSize = minSize;
        //自己启动自己
        this.start();
    }

    private void createWorkerTask() {
        WorkerTask task = new WorkerTask();
        //刚创建出来的线程应该是未使用的
        task.taskState = TaskState.FREE;
        WORKER_TASKS.add(task);
        task.start();
    }

    void submit(Runnable runnable) {
        if (destroy) {
            throw new IllegalStateException("线程池已销毁...");
        }
        synchronized (TASK_QUEUE) {
            //如果当前任务队超出队列限制,后续任务拒绝执行
            if (TASK_QUEUE.size() > queueSize) {
                discardPolicy.discard();
            }
            // 1.将任务添加到队列
            TASK_QUEUE.addLast(runnable);
            // 2.唤醒等待的线程去执行任务
            TASK_QUEUE.notifyAll();
        }
    }

    void shutdown() throws InterruptedException {
        int activeCount = THREAD_GROUP.activeCount();
        while (!TASK_QUEUE.isEmpty() && activeCount > 0) {
            // 如果还有任务,那就休息一会
            Thread.sleep(100);
        }
        //如果线程池中没有线程,那就不用关了
        int intVal = WORKER_TASKS.size();
        while (intVal > 0) {
            for (WorkerTask task : WORKER_TASKS) {
                //当任务队列为空的时候，线程状态才会为 BLOCKED ，所以可以打断掉，相反等任务执行完在关闭
                if (task.taskState == TaskState.BLOCKED) {
                    task.close();
                    intVal--;
                } else {
                    Thread.sleep(50);
                }
            }
        }
        this.destroy = true;
        //资源回收
        TASK_QUEUE.clear();
        WORKER_TASKS.clear();
        this.interrupt();
        System.out.println("线程关闭");
    }

    @Override
    public void run() {
        while (!destroy){
            try {
                Thread.sleep(5_000L);
                if(TASK_QUEUE.size() > activeSize && threadPoolSize < activeSize){
                    // 第一次扩容到 activeSize 大小
                    for(int i=threadPoolSize;i<activeSize;i++){
                        createWorkerTask();
                    }
                    this.threadPoolSize = activeSize;
                    System.out.println("[初次扩充] - [" + toString() + "]");
                }else if(TASK_QUEUE.size() > maxSize && threadPoolSize < maxSize){
                    // 第二次扩容到最大线程
                    for(int i=threadPoolSize; i<maxSize; i++){
                        createWorkerTask();
                    }
                    this.threadPoolSize = maxSize;
                    System.out.println("[再次扩充] - [" + toString() + "]");
                }else{
                    //防止线程在submit的时候，其他线程获取到锁干坏事
                    synchronized (WORKER_TASKS){
                        int releaseSize = threadPoolSize - activeSize;
                        Iterator<WorkerTask> iterator = WORKER_TASKS.iterator();
                        while (iterator.hasNext()){
                            if(releaseSize < 0){
                                break;
                            }
                            WorkerTask workerTask = iterator.next();
                            if(workerTask.taskState == TaskState.FREE){
                                workerTask.close();
                                iterator.remove();
                                releaseSize--;
                            }
                        }
                        System.out.println("[资源回收] - [" + toString() + "]");
                    }
                    threadPoolSize = activeSize;
                }
            } catch (InterruptedException e) {
                System.out.println("资源释放");
            }
        }
    }

    @Override
    public String toString() {
        return "SimpleThreadPoolExecutor{" +
                "threadPoolSize=" + threadPoolSize +
                ", taskQueueSize=" + TASK_QUEUE.size() +
                ", minSize=" + minSize +
                ", maxSize=" + maxSize +
                ", activeSize=" + activeSize +
                '}';
    }
}
