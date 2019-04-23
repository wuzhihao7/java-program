package com.keehoo.template;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/22
 */
public abstract class Game {
    /**
     * 初始化游戏
     */
    abstract void initialize();

    /**
     * 开始游戏
     */
    abstract void startPlay();

    /**
     * 结束游戏
     */
    abstract void endPlay();

    /**
     * 模板
     */
    public final void play(){
        initialize();
        startPlay();
        endPlay();
    }
}
