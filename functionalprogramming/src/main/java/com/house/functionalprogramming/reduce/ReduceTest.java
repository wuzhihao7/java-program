package com.house.functionalprogramming.reduce;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReduceTest {
    public static void main(String[] args) {
        System.out.println("reduce");
        List<Foo> fooList = Lists.newArrayList(
                new Foo("A", "san", 1.0, 2),
                new Foo("A", "nas", 13.0, 1),
                new Foo("B", "san", 112.0, 3),
                new Foo("C", "san", 43.0, 5),
                new Foo("B", "nas", 77.0, 7)
        );
        List<Bar> barList = Lists.newArrayList();
        Map<String, List<Foo>> collect = fooList
                .stream()
                .collect(Collectors.groupingBy(Foo::getName, Collectors.toList()));
        collect.forEach((name, fooListByName) -> {
            System.out.println(name+":"+fooListByName);
            Bar bar = new Bar();
            bar = fooListByName
                    .stream()
                    .reduce(bar, Bar::sum, (u, t) -> u);
            System.out.println(bar.toString());
            barList.add(bar);
        });
    }
}
