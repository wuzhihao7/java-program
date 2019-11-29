package com.technologysia.singleton.cas;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/4/22
 */
public class CasSingleton {
    private static final AtomicMarkableReference<CasSingleton> INSTANCE = new AtomicMarkableReference<>(null, false);

    private CasSingleton(){
    }

    public static CasSingleton getInstance(){
        while (true){
            CasSingleton casSingleton = INSTANCE.getReference();
            if(casSingleton != null){
                return casSingleton;
            }
            casSingleton = new CasSingleton();
            if(INSTANCE.compareAndSet(null, casSingleton, false, true)){
                return casSingleton;
            }
        }
    }
}
