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
