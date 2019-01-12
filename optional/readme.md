# Optional

## api

| 修饰符和类型             | 方法                                                         | 描述                                                         |
| ------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `static <T> Optional<T>` | `empty()`                                                    | 返回一个空`Optional`实例。                                   |
| `boolean`                | `equals(Object obj)`                                         | 指示某个其他对象是否“等于”此对象`Optional`。                 |
| `Optional<T>`            | `filter(Predicate<? super T> predicate)`                     | 如果存在值，并且值与给定谓词匹配，则返回`Optional`描述该值的值，否则返回空值`Optional`。 |
| `<U> Optional<U>`        | `flatMap(Function<? super T,? extends Optional<? extends U>> mapper)` | 如果存在`Optional`值，则返回将给定的 承载映射函数应用于该值的结果，否则返回空值`Optional`。 |
| `T`                      | `get()`                                                      | 如果存在值，则返回该值，否则抛出 `NoSuchElementException`。  |
| `int`                    | `hashCode()`                                                 | 返回值的哈希码（如果存在），否则`0` （零）如果不存在值。     |
| `void`                   | `ifPresent(Consumer<? super T> action)`                      | 如果存在值，则使用值执行给定操作，否则不执行任何操作。       |
| `void`                   | `ifPresentOrElse(Consumer<? super T> action,Runnable emptyAction)` | 如果存在值，则使用值执行给定操作，否则执行给定的基于空的操作。 |
| `boolean`                | `isEmpty()`                                                  | 如果值不存在，则返回`true`，否则 `false`。                   |
| `boolean`                | `isPresent()`                                                | 如果一个值存在，返回`true`，否则`false`。                    |
| `<U> Optional<U>`        | `map(Function<? super T,? extends U> mapper)`                | 如果存在值，则返回将给定映射函数应用于值的`Optional`描述（如同[`ofNullable(T)`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Optional.html#ofNullable(T))），否则返回空`Optional`。 |
| `static <T> Optional<T>` | `of(T value)`                                                | 返回`Optional`描述给定非`null` 值的内容。                    |
| `static <T> Optional<T>` | `ofNullable(T value)`                                        | 返回`Optional`描述给定值的值，如果为非`null`，否则返回空值`Optional`。 |
| `Optional<T>`            | `or(Supplier<? extends Optional<? extendsT>> supplier)`      | 如果存在`Optional`值，则返回描述该值的值，否则返回`Optional`由赋值函数生成的值。 |
| `T`                      | `orElse(T other)`                                            | 如果存在值，则返回该值，否则返回 `other`。                   |
| `T`                      | `orElseGet(Supplier<? extends T> supplier)`                  | 如果存在值，则返回该值，否则返回由供应函数生成的结果。       |
| `T`                      | `orElseThrow()`                                              | 如果存在值，则返回该值，否则抛出 `NoSuchElementException`。  |
| `<X extendsThrowable>T`  | `orElseThrow(Supplier<? extends X> exceptionSupplier)`       | 如果存在值，则返回该值，否则抛出异常提供函数生成的异常。     |
| `Stream<T>`              | `stream()`                                                   | 如果存在[`Stream`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Stream.html)值，则返回仅包含该值的顺序，否则返回空值`Stream`。 |
| `String`                 | `toString()`                                                 | 返回`Optional` 适合调试的非空字符串表示形式。                |

## 用法

1. empty() 返回一个空的`Optional`实例  

```java
Optional<String> empty = Optional.empty();
```

2. equals(Object obj) 判断当前`Optional`实例是否和其他实例相等  

```java
		Optional<Object> empty = Optional.empty();
        String a = new String("a");
        Optional<String> a1 = Optional.of(a);
        Optional<String> a2 = Optional.of(new String("a"));
        System.out.println(empty.equals(Optional.empty()));//都是空的Optional实例，true
        System.out.println(empty.equals(a));//a不是Optional实例，false
        System.out.println(a1.equals(a2));//Optional实例的属性相等（equal to），true
```

3. filter(Predicate<? superT> predicate) 如果存在值，并且值与给定谓词匹配，则返回`Optional`描述该值的值，否则返回空的`Optional`。  

```java
System.out.println(Optional.empty().filter(a -> "b".equals(a)));////Optional.empty
System.out.println(Optional.of(new String("a")).filter(a -> "b".equals(a)));//Optional.empty
System.out.println(Optional.of(new String("a")).filter(a -> "a".equals(a)));//Optional[a]
```

4. flatMap(Function<? super T,? extendsOptional<? extends U>> mapper) 如果存在`Optional`值，则返回将给定的 承载映射函数应用于该值的结果，否则返回空的`Optional`。  

```java
System.out.println(Optional.of("house").flatMap(s -> Optional.of(s.toLowerCase())));//Optional[HOUSE]
//Optional.of("house").flatMap(s -> s.toLowerCase());// flatMap需要自己封装成Optional
```

5. get() 如果存在值，则返回该值，否则抛出 `NoSuchElementException`。  

```java
System.out.println(Optional.of("house").get());//house
System.out.println(Optional.empty().get());//NoSuchElementException
```

6. ifPresent(Consumer<? super T> action) 如果存在值，则使用值执行给定操作，否则不执行任何操作。

```java
        Optional.empty().ifPresent(s -> System.out.println(s));
        System.out.println("--------------------------------");
        Optional.of("house").ifPresent(s -> System.out.println(s));//house
```

7. isPresent() 如果存在值，返回true，否则返回false  

```java
        System.out.println(Optional.empty().isPresent());//false
        System.out.println(Optional.of("house").isPresent());//true
```

8. map(Function<? super T,? extends U> mapper) 如果存在值，则返回将给定映射函数应用于值的`Optional`描述（如同 [`ofNullable(T)`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Optional.html#ofNullable(T))），否则返回空`Optional`。如果映射函数返回`null`结果，则此方法返回空`Optional`。

```java
System.out.println(Optional.empty().map(s -> s));//Optional.empty
System.out.println(Optional.of("house").map(s -> null));//Optional.empty
System.out.println(Optional.of("house").map(s -> s.toUpperCase()));//Optional[HOUSE]
```

9. of(T value)  返回`Optional`描述给定非`null` 值的内容。如果是null，则抛出`NullPointerException`  

```java
System.out.println(Optional.of("house"));//Optional[house]
System.out.println(Optional.of(null));//java.lang.NullPointerException
```

10. ofNullable(T value) 返回`Optional`描述给定值的值，如果为非`null`，否则返回空值`Optional`。

```java
System.out.println(Optional.ofNullable("house"));//Optional[house]
System.out.println(Optional.ofNullable(null));//Optional.empty
```

11. orElse(T other) 如果存在值，则返回该值，否则返回 `other`。

```java
System.out.println(Optional.ofNullable("house").orElse("ll"));//house
System.out.println(Optional.ofNullable("").orElse("ll"));//""
System.out.println(Optional.empty().orElse("ll"));//ll
```

12. orElseGet(Supplier<? extends T> other) 如果存在值，则返回该值，否则返回由供应函数生成的结果。

```java
System.out.println(Optional.ofNullable("house").orElseGet(() -> "ll"));//house
System.out.println(Optional.ofNullable("").orElseGet(() -> "ll"));//""
System.out.println(Optional.empty().orElseGet(() -> "ll"));//ll
```

13. orElseThrow(Supplier<? extends X> exceptionSupplier) 如果存在值，则返回该值，否则抛出异常提供函数生成的异常。

```java
System.out.println(Optional.ofNullable("house").orElseThrow(NullPointerException::new));//house
System.out.println(Optional.empty().orElseThrow(NullPointerException::new));//java.lang.NullPointerException
```

14. ifPresentOrElse(Consumer<? super T> action,Runnable emptyAction) 如果存在值，则使用值执行给定操作，否则执行给定的基于空的操作。

```java
Optional.of("house").ifPresentOrElse(s -> System.out.println(s), () ->  System.out.println("null"));//house
Optional.empty().ifPresentOrElse(s -> System.out.println(s), () ->  System.out.println("null"));//null
```

15. or(Supplier<? extends Optional<? extendsT>> supplier) 如果存在`Optional`值，则返回描述该值的值，否则返回`Optional`由赋值函数生成的值。

```java
System.out.println(Optional.of("house").or(() -> Optional.of("ll")));//Optional[house]
System.out.println(Optional.empty().or(() -> Optional.of("ll")));//Optional[ll]
```

16. `  `orElseThrow() 如果存在值，则返回该值，否则抛出 `NoSuchElementException`。

```java
System.out.println(Optional.of("house").orElseThrow());//house
System.out.println(Optional.empty().orElseThrow());//java.util.NoSuchElementException: No value present
```

17. stream() 如果存在[`Stream`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Stream.html)值，则返回仅包含该值的顺序，否则返回空值`Stream`。

```java
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
```

