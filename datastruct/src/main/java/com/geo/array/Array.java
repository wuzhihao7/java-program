package com.geo.array;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/13
 */
public class Array {
    private String[] strArray;
    /**
     * 数组元素个数
     */
    private int length;

    /**
     * 构造方法，传入数组最大长度
     */
    public Array(int max){
        strArray = new String[max];
    }

    /**
     * 检查数组是否包含某个元素，如果存在则返回其下标，如果不存在则返回-1
     * @param target 目标元素
     * @return 存在则返回下标，不存在返回-1
     */
    public int contains(String target){
        int index = -1;
        for(int i=0;i<length;i++){
            if(strArray[i].equals(target)){
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 插入一个元素
     * @param elem 元素
     */
    public void insert(String elem){
        strArray[length] = elem;
        length++;
    }

    public boolean remove(String target){
        int index = -1;
        if((index = contains(target)) != -1){
            for (int i=index; i<length; i++){
                //删除元素之后的所有元素前移一位
                strArray[i] = strArray[i+1];
            }
            length--;
            return true;
        }
        return false;
    }

    /**
     * 列出所有元素
     */
    public void display(){
        if(length > 0){
            for(int i=0;i<length;i++){
                System.out.print(strArray[i]+"\t");
            }
            System.out.println();
        }
    }
}
