package com.house.exception.example;

import com.house.exception.SystemException;

/**
 * @author wuzhihao
 * @date 2019/1/10
 */
public class SystemCodeTest {
    public static void main(String[] args){
        try {
            throw new SystemException("测试", null);
//            throw new SystemException("测试", SystemCode.SYSTEM_ERROR).set("key","value");
        }catch (SystemException e){
            try{
                throw SystemException.wrap(e, SystemCode.SYSTEM_ERROR2);
            }catch (SystemException e1){
                System.out.println(e1.getErrorCode() == SystemCode.SYSTEM_ERROR2);
                e1.printStackTrace();
            }
        }
    }
}
