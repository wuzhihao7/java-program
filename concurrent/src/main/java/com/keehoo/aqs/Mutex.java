package com.keehoo.aqs;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Mutex implements Lock, Serializable {
    private static class Sync extends AbstractQueuedSynchronizer{

        /**
         * report whether in locked state
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * acquires the lock if state is zero
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            //otherwise unused
            assert arg == 1;
            if(compareAndSetState(0, 1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * release the lock by setting state to zero
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            //otherwise unused
            assert arg == 1;
            if(getState() == 0){
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        /**
         * provides a condition
         * @return
         */
        Condition newCondition(){
            return new ConditionObject();
        }

        /**
         * deserializes properly
         * @param s
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            //reset to unlocked state
            setState(0);
        }
    }

    //the sync object does all the hard work.we just forward to it.
    private final Sync sync = new Sync();

    /**
     * 使用同步器的模板方法实现自己的同步语义
     */
    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
