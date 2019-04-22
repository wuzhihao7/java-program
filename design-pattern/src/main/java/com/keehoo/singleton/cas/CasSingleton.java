package com.keehoo.singleton.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/22
 */
public class CasSingleton {
    private static final AtomicReference<CasSingleton> INSTANCE = new AtomicReference<>();

    private CasSingleton(){
    }

    public static CasSingleton getInstance(){
        while (true){
            CasSingleton casSingleton = INSTANCE.get();
            if(casSingleton != null){
                return casSingleton;
            }
            casSingleton = new CasSingleton();
            if(INSTANCE.compareAndSet(null, casSingleton)){
                return casSingleton;
            }
        }
    }
}
