package com.keehoo.interceptingfilter;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/18
 */
public class Target {
    public void execute(String request){
        System.out.println("Executing request: " + request);
    }
}
