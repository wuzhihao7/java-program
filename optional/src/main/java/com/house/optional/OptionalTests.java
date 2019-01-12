package com.house.optional;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author wuzhihao
 * @date 2019/1/11
 */
public class OptionalTests {

    @Test
    public void testEmpty() {
        Optional<String> empty = Optional.empty();
    }

    @Test
    public void testEquals() {
        Optional<Object> empty = Optional.empty();
        String a = new String("a");
        Optional<String> a1 = Optional.of(a);
        Optional<String> a2 = Optional.of(new String("a"));
        System.out.println(empty.equals(Optional.empty()));
        System.out.println(empty.equals(a));
        System.out.println(a1.equals(a2));
    }

    @Test
    public void testFilter() {
        System.out.println(Optional.empty().filter(a -> "b".equals(a)));
        System.out.println(Optional.of(new String("a")).filter(a -> "b".equals(a)));
        System.out.println(Optional.of(new String("a")).filter(a -> "a".equals(a)));
    }

    @Test
    public void testFlatMap() {
        System.out.println(Optional.of("house").flatMap(s -> Optional.of(s.toLowerCase())));
//        Optional.of("house").flatMap(s -> s.toLowerCase());// flatMap需要自己封装成Optional
    }

    @Test
    public void testGet() {
        System.out.println(Optional.of("house").get());
        System.out.println(Optional.empty().get());
    }

    @Test
    public void testIfPresent() {
        Optional.empty().ifPresent(s -> System.out.println(s));
        System.out.println("--------------------------------");
        Optional.of("house").ifPresent(s -> System.out.println(s));
    }

    @Test
    public void testIsPresent() {
        System.out.println(Optional.empty().isPresent());
        System.out.println(Optional.of("house").isPresent());
    }

    @Test
    public void testMap() {
        System.out.println(Optional.empty().map(s -> s));
        System.out.println(Optional.of("house").map(s -> null));
        System.out.println(Optional.of("house").map(s -> s.toUpperCase()));
    }

    @Test
    public void testOf() {
        System.out.println(Optional.of("house"));
        System.out.println(Optional.of(null));
    }

    @Test
    public void testOfNullable() {
        System.out.println(Optional.ofNullable("house"));
        System.out.println(Optional.ofNullable(null));
    }

    @Test
    public void testOrElse() {
        System.out.println(Optional.ofNullable("house").orElse("ll"));
        System.out.println(Optional.ofNullable("").orElse("ll"));
        System.out.println(Optional.empty().orElse("ll"));
    }

    @Test
    public void testOrElseGet() {
        System.out.println(Optional.ofNullable("house").orElseGet(() -> "ll"));
        System.out.println(Optional.ofNullable("").orElseGet(() -> "ll"));
        System.out.println(Optional.empty().orElseGet(() -> "ll"));
    }

    @Test
    public void testOrElseThrow() {
        System.out.println(Optional.ofNullable("house").orElseThrow(NullPointerException::new));
        System.out.println(Optional.empty().orElseThrow(NullPointerException::new));
    }

    @Test
    public void testOr() {
        System.out.println(Optional.of("house").or(() -> Optional.of("ll")));
        System.out.println(Optional.empty().or(() -> Optional.of("ll")));
    }

    @Test
    public void testIfPresentOrElse() {
        Optional.of("house").ifPresentOrElse(s -> System.out.println(s), () -> System.out.println("null"));
        Optional.empty().ifPresentOrElse(s -> System.out.println(s), () -> System.out.println("null"));
    }

    @Test
    public void testOrElseThrow2() {
        System.out.println(Optional.of("house").orElseThrow());
        System.out.println(Optional.empty().orElseThrow());
    }

    @Test
    public void testStream() {
        List<String> list = new ArrayList<>();
        list.add("xxx1");
        list.add("xxx2");
        list.add("xxx3");

        Optional<List<String>> optional = Optional.ofNullable(list);
        //optional 可以转为stream
        optional.stream()
                .forEachOrdered(x -> x.stream().forEach(System.out::println));

        list.add("yyy1");
        Optional<List<String>> optional2 = Optional.ofNullable(list);
        //optional 扁平化
        optional2.stream()
                .flatMap(x -> x.stream())
                .forEach(System.out::println);
    }
}
