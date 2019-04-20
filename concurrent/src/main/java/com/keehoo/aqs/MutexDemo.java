package com.keehoo.aqs;

import java.util.stream.IntStream;

public class MutexDemo {
    private static Mutex mutex = new Mutex();

    public static void main(String[] args) {
        IntStream.range(0,10).forEach(
                i -> {
                    new Thread(){
                        @Override
                        public void run() {
                            mutex.lock();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }finally {
                                mutex.unlock();
                            }
                        }
                    }.start();
                }
        );
    }
}
