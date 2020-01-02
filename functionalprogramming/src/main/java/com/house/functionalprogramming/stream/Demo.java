package com.house.functionalprogramming.stream;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.*;

/**
 * @author wuzhihao
 * @date 2019/1/12
 */
public class Demo {
    private ArrayList<Student> stuList;
    private Student[] students;

    @Before
    public void init(){
        var random = new Random();
        stuList = new ArrayList<Student>() {
            {
                for (int i = 0; i < 10; i++) {
                    add(new Student("student" + i, random.nextInt(50) + 50));
                }
            }
        };

        students = new Student[10];
        for (int i=0;i<3;i++){
            Student student = new Student("user1",i);
            students[i] = student;
        }
        for (int i=3;i<6;i++){
            Student student = new Student("user2",i);
            students[i] = student;
        }
        for (int i=6;i<10;i++){
            Student student = new Student("user3",i);
            students[i] = student;
        }
    }

    @Test
    public void test1() {
        List<String> studentList = stuList.stream()
                .filter(x->x.getScore()>85)
                .sorted(Comparator.comparing(Student::getScore).reversed())
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println(studentList);
    }

    @Test
    public void test2(){
        Stream<String> stream = Stream.generate(() -> "user").limit(1);
        stream.forEach(System.out::println);
        stream.forEach(System.out::println);
    }

    @Test
    public void test3(){
        Stream<Student> stream = Stream.of(stuList.toArray(new Student[0])).filter(student -> {
            System.out.println("in filter");
            return student.getScore() > 85;
        });
        System.out.println("after collect");
        List<Student> collect = stream.collect(Collectors.toList());
    }

    @Test
    public void test4(){
        /**
         * 延迟执行特性，在聚合操作之前都可以添加相应元素
         */
        Stream<Student> stream = stuList.stream();
        stuList.add(new Student("student11", 100));
        System.out.println(stream.distinct().count());
    }

    @Test
    public void test5(){
        Stream<Student> stream = stuList.stream();
        stream.forEach(student -> {
            System.out.println("student->"+student);
            if("student6".equals(student.getName())){
                System.out.println("match->"+student);
                stuList.remove(student);
                System.out.println(stuList);
            }
        });
    }

    @Test
    public void test6(){
        //基本类型
        int[] ints = {1, 2, 3, 4};
        IntStream intStream = Arrays.stream(ints);
        //引用类型
        Student[] students = {new Student("s1", 1), new Student("s2", 2)};
        Stream<Student> studentStream = Arrays.stream(students);

        Stream<Integer> intStream2 = Stream.of(5, 6, 7, 8);

        Stream<int[]> intsStream = Stream.of(ints, ints);
        intsStream.forEach(System.out::println);
    }

    @Test
    public void test7(){
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        //创建普通流
        Stream<Integer> stream = list.stream();
        //创建并行流
        Stream<Integer> parallelStream = list.parallelStream();
    }

    @Test
    public void test8(){
        try {
            Stream<String> lines = Files.lines(Paths.get("filename"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test9(){
        Stream<Object> empty = Stream.empty();
    }

    @Test
    public void test10(){
        //创建无限流，通过limit提取指定大小
        Stream.generate(() -> "number"+new Random().nextInt()).limit(5).forEach(System.out::println);
        Stream.generate(() -> new Student("name",10)).limit(5).forEach(System.out::println);
        Stream.generate(() -> 1).limit(5).forEach(System.out::println);
    }

    @Test
    public void test11(){
        Stream.iterate(0, i -> i+1).limit(2).forEach(System.out::println);
        Stream.iterate(0, i -> i).limit(2).forEach(System.out::println);
        Stream.iterate(0, UnaryOperator.identity()).limit(2).forEach(System.out::println);
        Stream.iterate("a", UnaryOperator.identity()).limit(2).forEach(System.out::println);
    }

    @Test
    public void test12(){
        Stream.of("a","b","c").map(String::toUpperCase).forEach(System.out::println);
    }

    @Test
    public void test13(){
        Stream.of("a","b","c").filter(s -> "a".equals(s)).forEach(System.out::println);
    }

    @Test
    public void test14(){
        String[] arr1 = {"a", "b", "c"};
        String[] arr2 = {"d", "e", "f"};
        Stream.of(arr1,arr2).flatMap(Arrays::stream).forEach(System.out::println);
    }

    @Test
    public void test15(){
        /**
         * Comparator.comparing是一个键提取的功能
         * 以下两个语句表示相同意义
         */
        String[] arr1 = {"abc","a","bc","abcd"};
        Arrays.stream(arr1).sorted(
                (x,y) ->{
                    if(x.length() > y.length()){
                        return 1;
                    }else if(x.length() < y.length()){
                        return -1;
                    }else {
                        return 0;
                    }
                }
        ).forEach(System.out::println);
        System.out.println("-----------------------------");
        Stream.of(arr1).sorted(Comparator.comparing(String::length)).forEach(System.out::println);
        System.out.println("-----------------------------");
        //Comparator. naturalOrder()：返回一个自然排序比较器，用于比较对象（Stream里面的类型必须是可比较的）
        Arrays.stream(arr1).sorted(Comparator.naturalOrder()).forEach(System.out::println);
        System.out.println("-----------------------------");
        /**
         * 倒序
         * reversed(),java8泛型推导的问题，所以如果comparing里面是非方法引用的lambda表达式就没办法直接使用reversed()
         * Comparator.reverseOrder():也是用于翻转顺序，用于比较对象（Stream里面的类型必须是可比较的）
         */
        Arrays.stream(arr1).sorted(Comparator.comparing(String::length).reversed()).forEach(System.out::println);
        System.out.println("-----------------------------");
        Arrays.stream(arr1).sorted(Comparator.reverseOrder()).forEach(System.out::println);
        System.out.println("-----------------------------");
        /**
         * thenComparing
         * 先按照首字母排序
         * 之后按照String的长度排序
         */
        Arrays.stream(arr1).sorted(Comparator.comparing((String s) -> s.charAt(0)).thenComparing(String::length)).forEach(System.out::println);
    }

    @Test
    public void test16(){
        Stream.iterate(1,x->x+2).limit(10).forEach(System.out::println);
    }

    @Test
    public void test17(){
        Stream.iterate(1,x->x+2).skip(2).limit(5).forEach(System.out::println);
    }

    @Test
    public void test18(){
        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4);
        Stream<Integer> stream2 = Stream.of(3, 4, 5, 6);
//        Stream.concat(stream1,stream2).forEach(System.out::print);
        Stream.concat(stream1,stream2).distinct().forEach(System.out::print);
    }

    @Test
    public void test19(){
        Stream.of(1, 2, 3, 4).max(Integer::compareTo).ifPresent(System.out::println);
        Stream.of(1, 2, 3, 4).min(Integer::compareTo).ifPresent(System.out::println);
    }

    @Test
    public void test20(){
        System.out.println(Stream.of("a","b","c","d").count());
    }

    @Test
    public void test21(){
        Stream.of(1,2,3,4,5).findFirst().ifPresent(System.out::println);
    }

    @Test
    public void test22(){
        Stream.of(1,2,3,4,5).filter(i -> i > 3).findAny().ifPresent(System.out::println);
        Stream.of(1,2,3,4,5).parallel().filter(i -> i > 3).findAny().ifPresent(System.out::println);
    }

    @Test
    public void test23(){
        System.out.println(Stream.of("b","ab","abc","abcd","abcde").anyMatch(x -> x.startsWith("a")));
        System.out.println(Stream.of("b","ab","abc","abcd","abcde").anyMatch(x -> x.startsWith("aa")));
    }

    @Test
    public void test24(){
        Stream.of(1,2,3,4,5,5,6).distinct().forEach(System.out::println);
    }

    @Test
    public void test25(){
        System.out.println(Stream.of(1,2,3,4).allMatch(i -> i > 0));
        System.out.println(Stream.of(1,2,3,4).allMatch(i -> i > 1));
    }

    @Test
    public void test26(){
        System.out.println(Stream.of(1,2,3,4).noneMatch(i -> i>5));
        System.out.println(Stream.of(1,2,3,4).noneMatch(i -> i>3));
    }

    @Test
    public void test27(){
        System.out.println(Stream.of(1,2,3,4).reduce(0, (a,b) -> a+b));
        System.out.println(Stream.of(1,2,3,4).reduce(0, Integer::sum));
        System.out.println(Stream.of(1,2,3,4).reduce(Integer::sum).get());
        System.out.println(Stream.of(1,2,3,4).reduce(1, (a,b) -> a*b));
        System.out.println(Stream.of(1,2,3,4).reduce((a,b) -> a*b).get());
    }

    @Test
    public void test28(){
        System.out.println(Stream.of(1,2,3,4,4,5).collect(Collectors.toList()));
        System.out.println(Stream.of(1,2,3,4,4,5).collect(Collectors.toSet()));
        List<Student> stuList = new ArrayList<>();
        //针对重复key的  覆盖之前的value
        stuList.add(new Student("a", 1));
        stuList.add(new Student("a", 2));
        System.out.println(stuList.stream().collect(Collectors.toMap(Student::getName, Student::getScore, (key, value) -> value)));
        //针对重复key的  覆盖之前的value,value为空,直接存放  不调用map.merge,其中lambda表达式: (k,v)->v   不会被调用,但是又不能为空
        stuList.add(new Student("b", null));
        HashMap<String, Integer> collect = stuList.stream().collect(Collector.of(HashMap::new, (m, per) -> m.put(per.getName(), per.getScore()), (key, value) -> value, Collector.Characteristics.IDENTITY_FINISH));
        System.out.println(collect);

        HashSet<Student> studentHashSet = stuList.stream().collect(Collectors.toCollection(HashSet::new));
        System.out.println(studentHashSet);

        IntSummaryStatistics summaryStatistics = this.stuList.stream().collect(Collectors.summarizingInt(Student::getScore));
        System.out.println("getAverage->"+summaryStatistics.getAverage());
        System.out.println("getMax->"+summaryStatistics.getMax());
        System.out.println("getMin->"+summaryStatistics.getMin());
        System.out.println("getCount->"+summaryStatistics.getCount());
        System.out.println("getSum->"+summaryStatistics.getSum());
    }

    public void test29(){
        System.out.println(Arrays.toString(stuList.stream().toArray()));
        System.out.println(Arrays.toString(stuList.stream().toArray(Student[]::new)));
    }

    @Test
    public void test30(){
        Map<String,List<Student>> map1 = Arrays.stream(students).collect(Collectors.groupingBy(Student::getName));
        map1.forEach((x,y)-> System.out.println(x+"->"+y));
        //如果只有两类，使用partitioningBy会比groupingBy更有效率
        Map<Boolean,List<Student>> map2 = Arrays.stream(students).collect(Collectors.partitioningBy(x->x.getScore()>5));
        map2.forEach((x,y)-> System.out.println(x+"->"+y));
        //downstream指定类型
        Map<String, Set<Student>> setMap = Arrays.stream(students).collect(Collectors.groupingBy(Student::getName, Collectors.toSet()));
        System.out.println(setMap);
        //counting
        Map<String, Long> longMap = Arrays.stream(students).collect(Collectors.groupingBy(Student::getName, Collectors.counting()));
        System.out.println(longMap);
        //summingInt
        Map<String, Integer> integerMap = Arrays.stream(students).collect(Collectors.groupingBy(Student::getName, Collectors.summingInt(Student::getScore)));
        System.out.println(integerMap);
        //maxBy
        Map<String, Optional<Student>> optionalMap = Arrays.stream(students).collect(Collectors.groupingBy(Student::getName, Collectors.maxBy(Comparator.comparing(Student::getScore))));
        System.out.println(optionalMap);
        //mapping
        Map<String, Set<Integer>> stringSetMap = Arrays.stream(students).collect(Collectors.groupingBy(Student::getName, Collectors.mapping(Student::getScore, Collectors.toSet())));
        System.out.println(stringSetMap);
    }

    @Test
    public void test31(){
        DoubleStream doubleStream = DoubleStream.of(1.0d, 2.0d, 3.0d);
        Stream<Double> boxed = doubleStream.boxed();
        DoubleStream doubleStream1 = boxed.mapToDouble(Double::doubleValue);
        IntStream intStream = IntStream.of(1, 2, 3);
        IntStream rangeClosed = IntStream.rangeClosed(0, 10);//[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        IntStream range = IntStream.range(0, 10);//[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
    }

    @Test
    public void test32(){
        Stream.iterate(1, i -> i+1).limit(10)
                .peek(i -> System.out.println(Thread.currentThread().getName()+":->peek1->" + i)).filter(x -> x > 5)
                .peek(i -> System.out.println(Thread.currentThread().getName()+":->peek2->" + i)).filter(x -> x < 8)
                .peek(i -> System.out.println(Thread.currentThread().getName()+":->peek3->" + i))
                .forEach(System.out::println);
        Stream.iterate(1, i -> i+1).limit(10).parallel()
                .peek(i -> System.out.println(Thread.currentThread().getName()+":->peek1->" + i)).filter(x -> x > 5)
                .peek(i -> System.out.println(Thread.currentThread().getName()+":->peek2->" + i)).filter(x -> x < 8)
                .peek(i -> System.out.println(Thread.currentThread().getName()+":->peek3->" + i))
                .forEach(System.out::println);
    }
}
