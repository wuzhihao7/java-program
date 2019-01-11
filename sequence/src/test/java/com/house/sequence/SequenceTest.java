package com.house.sequence;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @date 2019/1/11
 */
public class SequenceTest {
    @Test
    public void test() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        IntStream.range(0,10).forEach(i -> pool.execute(() -> System.out.println(SequenceFactory.getSeq("aa").hashCode())));
    }
}
