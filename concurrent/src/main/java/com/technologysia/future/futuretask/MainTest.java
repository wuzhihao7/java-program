package com.technologysia.future.futuretask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class MainTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Computable<String, Integer> c = Integer::parseInt;
        Memoizerl<String, Integer> cache = new Memoizerl<>(c);
        Integer compute = cache.compute("10");
        System.out.println(compute);

        FutureTask<Integer> integerFutureTask = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("睡眠");
            return 10;
        });
        integerFutureTask.run();
        System.out.println("main");
        Integer integer = integerFutureTask.get();
        System.out.println(integer);
    }
}
