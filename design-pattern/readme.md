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

