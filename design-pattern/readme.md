# 接口与回调
callback是一种常见的设计模式，在这种模式中，可以指定在某个特定事件发生后应该采取的动作。

> 定义接口

```java
package com.technologysia.callback;

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
package com.technologysia.callback;

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
package com.technologysia.callback;

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
package com.technologysia.callback;

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



# 模板模式

在模板模式（Template Pattern）中，一个抽象类公开定义了执行它的方法的方式/模板。它的子类可以按需要重写方法实现，但调用将以抽象类中定义的方式进行。这种类型的设计模式属于行为型模式。

## 介绍

**意图：**定义一个操作中的算法的骨架，而将一些步骤延迟到子类中。模板方法使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。

**主要解决：**一些方法通用，却在每一个子类都重新写了这一方法。

**何时使用：**有一些通用的方法。

**如何解决：**将这些通用算法抽象出来。

**关键代码：**在抽象类实现，其他步骤在子类实现。

**应用实例：** 1、在造房子的时候，地基、走线、水管都一样，只有在建筑的后期才有加壁橱加栅栏等差异。 2、西游记里面菩萨定好的 81 难，这就是一个顶层的逻辑骨架。 3、spring 中对 Hibernate 的支持，将一些已经定好的方法封装起来，比如开启事务、获取 Session、关闭 Session 等，程序员不重复写那些已经规范好的代码，直接丢一个实体就可以保存。

**优点：** 1、封装不变部分，扩展可变部分。 2、提取公共代码，便于维护。 3、行为由父类控制，子类实现。

**缺点：**每一个不同的实现都需要一个子类来实现，导致类的个数增加，使得系统更加庞大。

**使用场景：** 1、有多个子类共有的方法，且逻辑相同。 2、重要的、复杂的方法，可以考虑作为模板方法。

**注意事项：**为防止恶意操作，一般模板方法都加上 final 关键词。

## 实现

我们将创建一个定义操作的 *Game* 抽象类，其中，模板方法设置为 final，这样它就不会被重写。*Cricket* 和 *Football* 是扩展了 *Game* 的实体类，它们重写了抽象类的方法。

*TemplatePatternDemo*，我们的演示类使用 *Game* 来演示模板模式的用法。

![æ¨¡æ¿æ¨¡å¼ç UML å¾](http://www.runoob.com/wp-content/uploads/2014/08/template_pattern_uml_diagram.jpg)



