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
