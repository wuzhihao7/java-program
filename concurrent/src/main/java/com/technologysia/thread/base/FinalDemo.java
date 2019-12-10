package com.technologysia.thread.base;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/14
 */
public class FinalDemo {
    private int a;
    private final int b;
    private static FinalDemo finalDemo;

    public FinalDemo(){
        a = 1;
        b = 2;
    }

    public static void write(){
        finalDemo = new FinalDemo();
    }

    public static void read(){
        FinalDemo demo = finalDemo;
        int a = demo.a;
        int b = demo.b;
    }
}
