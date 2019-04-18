package com.keehoo.interceptingfilter;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/18
 */
public class DebugFilter implements Filter {
    @Override
    public void execute(String request) {
        System.out.println("request log: " + request);
    }
}
