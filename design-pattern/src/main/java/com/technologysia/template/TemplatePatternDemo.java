package com.technologysia.template;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/23
 */
public class TemplatePatternDemo {
    public static void main(String[] args) {
        Game game = new Cricket();
        game.play();
        System.out.println();
        game = new Football();
        game.play();
    }
}
