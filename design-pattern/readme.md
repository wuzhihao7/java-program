# 接口与回调
callback是一种常见的设计模式，在这种模式中，可以指定在某个特定事件发生后应该采取的动作。

> 定义接口

```java
package com.keehoo.callback;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/11
 */
public interface IScore {
    void recordScore(float timeSpend);
}

```

> 创建Teacher类并实现接口

```java
package com.keehoo.callback;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/11
 */
public class Teacher implements IScore {
    public void publishTask(Student student, int length) {
        System.out.println("Teacher publish the task, run " + length + "ｍ.");
//        student.doRun(this, length);
        new Thread(){
            @Override
            public void run() {
                student.doRun(Teacher.this, length);
            }
        }.start();
        System.out.println("Teacher waiting...");
    }

    @Override
    public void recordScore(float timeSpend) {
        if (timeSpend < 7) {
            System.out.println("Well done. Excellent.");
        } else {
            System.out.println("Good");
        }
    }
}

```

> 创建Student类

```java
package com.keehoo.callback;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/11
 */
public class Student {
    public void doRun(IScore score, int length)
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (length == 50) {
            System.out.println("Begin to Run.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Run finished, my time spend is 6.8s.");
        }
        score.recordScore(6.8f);
    }
}

```

> 测试

```java
package com.keehoo.callback;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/11
 */
public class MainTest {
    public static void main(String[] args) {
        Teacher teacher = new Teacher();
        Student student = new Student();
        teacher.publishTask(student, 50);
    }
}

```

# 拦截过滤器模式
拦截过滤器模式（Intercepting Filter Pattern）用于对应用程序的请求或响应做一些预处理/后处理。定义过滤器，并在把请求传给实际目标应用程序之前应用在请求上。过滤器可以做认证/授权/记录日志，或者跟踪请求，然后把请求传给相应的处理程序。以下是这种设计模式的实体。
- 过滤器（Filter） - 过滤器在请求处理程序执行请求之前或之后，执行某些任务。
- 过滤器链（Filter Chain） - 过滤器链带有多个过滤器，并在 Target 上按照定义的顺序执行这些过滤器。
- Target - Target 对象是请求处理程序。
- 过滤管理器（Filter Manager） - 过滤管理器管理过滤器和过滤器链。
- 客户端（Client） - Client 是向 Target 对象发送请求的对象。

## 实现

![实现UML图](http://www.runoob.com/wp-content/uploads/2014/08/interceptingfilter_pattern_uml_diagram.jpg)