package com.technologysia.future;

public class FutureData implements Data {
    private RealData realData;
    private boolean ready;

    public synchronized void setRealData(RealData realData) {
        if (ready) {
            return;
        }
        this.realData = realData;
        ready = true;
        //通知所有等待的线程继续运行
        notifyAll();
    }

    @Override
    public synchronized String getResult() {
        while (!ready){
            try {
                System.out.println("wait...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.getResult();
    }
}
