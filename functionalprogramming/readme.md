# 函数式编程

## lambda

### 用法

```java
    @FunctionalInterface
    interface Hello{
        void say(String msg);
    }
    @Test
    public void testLambda(){
        Hello say = x -> System.out.println(x);
        say.say("你好");
    }
```

### 四大核心函数型接口

1. 消费型接口（有参无返回值）

```java
public interface Consumer<T> {
    void accept(T t);
}
```

2. 供给型接口（无参有返回值）

```java
public interface Supplier<T>{
    T get();
}
```

3. 函数型接口（有参有返回值）

```java
public interface Function<T, R>{
    R apply(T t);
}
```

4. 断言型接口（有参有布尔返回值）

```java
public interface Predicate<T>{
    boolean test(T t);
}
```

### 方法引用

语法：  

```
对象::实例方法
类::静态方法
类::实例方法
```

### 构造引用

语法：  

```
类名::new
```

## Stream

### 特性

- stream不存储数据  
- stream不改变源数据  
- stream的延迟执行特性  

1. 在数组或集合的基础上创建stream，stream不会专门存储数据，对stream的操作也不会影响到创建它的数组和集合，对于stream的聚合、消费或收集操作只能进行一次，再次操作会报错  

```java
        Stream<String> stream = Stream.generate(() -> "user").limit(1);
        stream.forEach(System.out::println);//user
        stream.forEach(System.out::println);//java.lang.IllegalStateException: stream has already been operated upon or closed
```

2. stream的操作是延迟执行的，在collect方法执行之前，filter、sorted、map方法还未执行，只有当collect方法执行时才会触发之前转换操作

```java
        Stream<Student> stream = Stream.of(stuList.toArray(new Student[stuList.size()])).filter(student -> {
            System.out.println("in filter");
            return student.getScore() > 85;
        });
        System.out.println("after collect");
        List<Student> collect = stream.collect(Collectors.toList());
```

3. 当我们操作一个流的时候，并不会修改流底层的集合（即使集合是线程安全的），如果想要修改原有的集合，就无法定义流操作的输出，由于stream的延迟执行特性，在聚合操作执行前修改数据源是允许的。

```java
        /**
         * 延迟执行特性，在聚合操作之前都可以添加相应元素
         */
        Stream<Student> stream = stuList.stream();
        stuList.add(new Student("student11", 100));
        System.out.println(stream.distinct().count());
```

4. 延迟执行特性，会产生干扰

```java
        Stream<Student> stream = stuList.stream();
        stream.forEach(student -> {
            System.out.println("student->"+student);
            if("student6".equals(student.getName())){
                System.out.println("match->"+student);
                stuList.remove(student);
                System.out.println(stuList);
            }
        });//java.lang.NullPointerException
```

### 创建Stream

1. 通过数组创建

```java
        //基本类型
        int[] ints = {1, 2, 3, 4};
        IntStream intStream = Arrays.stream(ints);
        //引用类型
        Student[] students = {new Student("s1", 1), new Student("s2", 2)};
        Stream<Student> studentStream = Arrays.stream(students);

        Stream<Integer> intStream2 = Stream.of(5, 6, 7, 8);

        Stream<int[]> intsStream = Stream.of(ints, ints);
        intsStream.forEach(System.out::println);
```

2. 通过集合创建

```java
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        //创建普通流
        Stream<Integer> stream = list.stream();
        //创建并行流
        Stream<Integer> parallelStream = list.parallelStream();
```

3. 文件生成流

```java
        try {
            Stream<String> lines = Files.lines(Paths.get("filename"));
        } catch (IOException e) {
            e.printStackTrace();
        }
```

4. 创建空的流

```java
Stream<Object> empty = Stream.empty();
```

5. 创建无限流

```java
        //创建无限流，通过limit提取指定大小
        Stream.generate(() -> "number"+new Random().nextInt()).limit(5).forEach(System.out::println);
        Stream.generate(() -> new Student("name",10)).limit(5).forEach(System.out::println);
        Stream.generate(() -> 1).limit(5).forEach(System.out::println);
```

6. 创建规律的无限流

```java
        Stream.iterate(0, i -> i+1).limit(2).forEach(System.out::println);
        Stream.iterate(0, i -> i).limit(2).forEach(System.out::println);
        Stream.iterate(0, UnaryOperator.identity()).limit(2).forEach(System.out::println);
        Stream.iterate("a", UnaryOperator.identity()).limit(2).forEach(System.out::println);
```

### Stream操作

1. map 转换流，将一种类型的流转换为另外一种流  

```java
Stream.of("a","b","c").map(String::toUpperCase).forEach(System.out::println);
```

2. filter 过滤流，过滤流中的元素

```java
Stream.of("a","b","c").filter(s -> "a".equals(s)).forEach(System.out::println);
```

3. flapMap 拆解流，将流中每一个元素拆解成一个流

```java
        String[] arr1 = {"a", "b", "c"};
        String[] arr2 = {"d", "e", "f"};
        Stream.of(arr1,arr2).flatMap(Arrays::stream).forEach(System.out::println);
```

4. sorted() / sorted((T, T) -> int) 如果流中的元素的类实现了 Comparable 接口，即有自己的排序规则，那么可以直接调用 sorted() 方法对元素进行排序，如 Stream；反之, 需要调用 sorted((T, T) -> int) 实现 Comparator 接口

```java
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

```

5. limit，限制从流中获得前n个数据

```java
    Stream.iterate(1,x->x+2).limit(10).forEach(System.out::println);
```

6. skip，跳过前n个数据

```java
	Stream.iterate(1,x->x+2).skip(2).limit(5).forEach(System.out::println);
```

7. concat 把两个stream合并成一个stream（合并的stream类型必须相同）

```java
        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4);
        Stream<Integer> stream2 = Stream.of(3, 4, 5, 6);
//        Stream.concat(stream1,stream2).forEach(System.out::print);
        Stream.concat(stream1,stream2).distinct().forEach(System.out::print);
```

8. max/min 最大值/最小值

```java
        Stream.of(1, 2, 3, 4).max(Integer::compareTo).ifPresent(System.out::println);
        Stream.of(1, 2, 3, 4).min(Integer::compareTo).ifPresent(System.out::println);
```

9. count 统计

```java
System.out.println(Stream.of("a","b","c","d").count());
```

10. findFirst 查找第一个

```java
Stream.of(1,2,3,4,5).findFirst().ifPresent(System.out::println);
```

11. findAny 找到所有匹配的元素，对并行流十分有效，只要在任何片段发现了第一个匹配元素就会结束整个运算

```java
Stream.of(1,2,3,4,5).filter(i -> i > 3).findAny().ifPresent(System.out::println);
Stream.of(1,2,3,4,5).parallel().filter(i -> i > 3).findAny().ifPresent(System.out::println);
```

12. anyMatch 是否含有匹配元素

```java
System.out.println(Stream.of("b","ab","abc","abcd","abcde").anyMatch(x -> x.startsWith("a")));
        System.out.println(Stream.of("b","ab","abc","abcd","abcde").anyMatch(x -> x.startsWith("aa")));
```

13. distinct 去除重复元素，通过类的 equals 方法来判断两个元素是否相等

```java
Stream.of(1,2,3,4,5,5,6).distinct().forEach(System.out::println);
```

14. allMatch(T -> boolean) 流中是否所有元素都匹配给定的 T -> boolean 条件

```java
        System.out.println(Stream.of(1,2,3,4).allMatch(i -> i > 0));
        System.out.println(Stream.of(1,2,3,4).allMatch(i -> i > 1));
```

15. noneMatch(T -> boolean) 流中是否没有元素匹配给定的 T -> boolean 条件

```java
        System.out.println(Stream.of(1,2,3,4).noneMatch(i -> i>5));
        System.out.println(Stream.of(1,2,3,4).noneMatch(i -> i>3));
```

16.  reduce((T, T) -> T)/reduce(T, (T, T) -> T) 用于组合流中的元素，如求和，求积，求最大值等

```java
        System.out.println(Stream.of(1,2,3,4).reduce(0, (a,b) -> a+b));
        System.out.println(Stream.of(1,2,3,4).reduce(0, Integer::sum));
        System.out.println(Stream.of(1,2,3,4).reduce(Integer::sum).get());
        System.out.println(Stream.of(1,2,3,4).reduce(1, (a,b) -> a*b));
        System.out.println(Stream.of(1,2,3,4).reduce((a,b) -> a*b).get());
```

17. collect() coollect 方法作为终端操作，接受的是一个 Collector 接口参数，能对数据进行一些收集归总操作

```java
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
```

18. toArray 转为数组

```java
        System.out.println(Arrays.toString(stuList.stream().toArray()));
        System.out.println(Arrays.toString(stuList.stream().toArray(Student[]::new)));
```

19. 分组和分片 将collect的结果集展示位Map的形式

```java
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
```

### 原始类型流

1. 初始化与转换

```java
        DoubleStream doubleStream = DoubleStream.of(1.0d, 2.0d, 3.0d);
        Stream<Double> boxed = doubleStream.boxed();
        DoubleStream doubleStream1 = boxed.mapToDouble(Double::doubleValue);
        IntStream intStream = IntStream.of(1, 2, 3);
        IntStream rangeClosed = IntStream.rangeClosed(0, 10);//[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        IntStream range = IntStream.range(0, 10);//[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
```

### 并行流

```java
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
```



