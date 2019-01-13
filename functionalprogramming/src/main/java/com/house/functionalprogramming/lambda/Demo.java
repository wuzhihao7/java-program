package com.house.functionalprogramming.lambda;

import org.junit.Test;

/**
 * @author wuzhihao
 * @date 2019/1/12
 */
public class Demo {
    @FunctionalInterface
    interface Hello{
        void say(String msg);
    }
    @Test
    public void testLambda(){
        Hello say = x -> System.out.println(x);
        say.say("你好");
    }
}
