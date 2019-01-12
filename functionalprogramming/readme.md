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

