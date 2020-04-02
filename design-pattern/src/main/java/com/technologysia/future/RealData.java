package com.technologysia.future;

import java.util.concurrent.TimeUnit;

public class RealData implements Data {
    private final String result;

    public RealData(String result){
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.result = result;
    }
    @Override
    public String getResult() {
        return result;
    }
}
