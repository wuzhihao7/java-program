package com.house.functionalprogramming.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author wuzhihao
 * @date 2019/1/12
 */
public class Demo {
    @Test
    public void testStream(){
        List<String> streams = Arrays.asList("a", "b", "c", "d");
        streams.stream().filter(s -> s.startsWith("c")).map(String::toUpperCase).sorted().forEach(System.out::println);
    }

    @Test
    public void test2(){
        Arrays.asList("a1","a2","a3").stream().findFirst().ifPresent(System.out::println);
        Stream.of("a1","a2","a3").findFirst().ifPresent(System.out::println);
    }

    @Test
    public void test3(){
        IntStream.range(0,4).forEach(System.out::print);
        System.out.println();
        LongStream.range(0L,4L).forEach(System.out::print);
        System.out.println();
        Arrays.stream(new int[]{1,2,3}).average().ifPresent(System.out::println);
    }
}
