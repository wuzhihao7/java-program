# 函数式编程

## lambda

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

## stream

1. Stream 不是集合元素，它不是数据结构并不保存数据，它是有关算法和计算的，它更像一个高级版本的 Iterator。简单来说，它的作用就是通过一系列操作将数据源（集合、数组）转化为想要的结果。  

2. 特点：  
   - Stream 是不会存储元素的。  
   - Stream 不会改变原对象，相反，他们会返回一个持有结果的新Stream。  
   - Stream 操作是延迟执行的。意味着它们会等到需要结果的时候才执行。  

3. 生成stream的方式  

```java
//Collection系的 stream() 和 parallelStream();
List<String> list = new ArrayList<>();
Stream<String> stream = list.stream();
Stream<String> stringStream = list.parallelStream();

//通过Arrays
Stream<String> stream1 = Arrays.stream(new String[10]);

//通过Stream
Stream<Integer> stream2 = Stream.of(1, 2, 3);

//无限流
//迭代
Stream<Integer> iterate = Stream.iterate(0, (x) -> x + 2);
iterate.limit(10).forEach(System.out::println);

//生成
Stream<Double> generate = Stream.generate(() -> Math.random());
generate.forEach(System.out::println);
```

4. stream的中间操作

   多个中间操作连接而成为流水线，流水线不遇到终止操作是不触发任何处理的，所为又称为“惰性求值”。

```java
list.stream()
                .map(s -> s + 1)  //映射
                .flatMap(s -> Stream.of(s)) 
//和map差不多，但返回类型为Stream，类似list.add()和list.addAll()的区别
                .filter(s -> s < 1000)    //过滤
                .limit(5)   //限制
                .skip(1)    //跳过
                .distinct() //去重
                .sorted()   //自然排序
                .sorted(Integer::compareTo) //自定义排序
```

关于map方法，参数为一个Function函数型接口的对象，也就是传入一个参数返回一个对象。这个参数就是集合中的每一项。类似Iterator遍历。其它的几个操作思想都差不多。

5. stream的终止操作  

```java
list.stream().allMatch((x) -> x == 555); // 检查是否匹配所有元素
list.stream().anyMatch(((x) -> x>600)); // 检查是否至少匹配一个元素
list.stream().noneMatch((x) -> x>500); //检查是否没有匹配所有元素
list.stream().findFirst(); // 返回第一个元素
list.stream().findAny(); // 返回当前流中的任意一个元素
list.stream().count(); // 返回流中元素的总个数
list.stream().forEach(System.out::println); //内部迭代
list.stream().max(Integer::compareTo); // 返回流中最大值
Optional<Integer> min = list.stream().min(Integer::compareTo);//返回流中最小值
System.out.println("min "+min.get());
```

reduce （归约）：将流中元素反复结合起来得到一个值

```java
Integer reduce = list.stream()
        .map(s -> (s + 1))
        .reduce(0, (x, y) -> x + y);    
        //归约：0为第一个参数x的默认值，x是计算后的返回值，y为每一项的值。
System.out.println(reduce);

Optional<Integer> reduce1 = list.stream()
        .map(s -> (s + 1))
        .reduce((x, y) -> x + y); 
         // x是计算后的返回值，默认为第一项的值，y为其后每一项的值。
System.out.println(reduce);
```

collect（收集）：将流转换为其他形式。需要Collectors类的一些方法。

```java
//转集合
        Set<Integer> collect = list.stream()
                .collect(Collectors.toSet());

        List<Integer> collect2 = list.stream()
                .collect(Collectors.toList());

        HashSet<Integer> collect1 = list.stream()
                .collect(Collectors.toCollection(HashSet::new));

        //分组 {group=[444, 555, 666, 777, 555]}
        Map<String, List<Integer>> collect3 = list.stream()
                .collect(Collectors.groupingBy((x) -> "group"));//将返回值相同的进行分组
        System.out.println(collect3);

        //多级分组 {group={777=[777], 666=[666], 555=[555, 555], 444=[444]}}
        Map<String, Map<Integer, List<Integer>>> collect4 = list.stream()
                .collect(Collectors.groupingBy((x) -> "group", Collectors.groupingBy((x) -> x)));
        System.out.println(collect4);

        //分区 {false=[444], true=[555, 666, 777, 555]}
        Map<Boolean, List<Integer>> collect5 = list.stream()
                .collect(Collectors.partitioningBy((x) -> x > 500));
        System.out.println(collect5);

        //汇总
        DoubleSummaryStatistics collect6 = list.stream()
                .collect(Collectors.summarizingDouble((x) -> x));
        System.out.println(collect6.getMax());
        System.out.println(collect6.getCount());

        //拼接 444,555,666,777,555
        String collect7 = list.stream()
                .map(s -> s.toString())
                .collect(Collectors.joining(","));
        System.out.println(collect7);

        //最大值
        Optional<Integer> integer = list.stream()
                .collect(Collectors.maxBy(Integer::compare));
        System.out.println(integer.get());
```

