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
