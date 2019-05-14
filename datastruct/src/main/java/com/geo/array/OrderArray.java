package com.geo.array;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/14
 */
public class OrderArray {
    private int[] intArray;
    private int length;

    public OrderArray(int max){
        intArray = new int[max];
    }

    /**
     * 利用二分查找定位某个元素，如果存在返回其下标，不存在返回-1
     * @param target
     * @return
     */
    public int find(int target){
        int startIdx = 0;
        int endIdx = length-1;
        int curIdx;
        if(startIdx < 0){
            return -1;
        }
        while (true){
            curIdx = (startIdx+endIdx)/2;
            if(target == intArray[curIdx]){
                return curIdx;
            }else if(curIdx == startIdx){
                if(target == intArray[endIdx]){
                    return endIdx;
                }
                return -1;
            }else{
                if(intArray[curIdx] < target){
                    startIdx = curIdx;
                }else{
                    endIdx = curIdx;
                }
            }
        }
    }

    /**
     * 插入某个元素
     * @param elem 新增元素
     */
    public void insert(int elem){
        int location = 0;
        for(;location<length;location++){
            if(intArray[location] > elem){
                break;
            }
        }
        for(int i = length; i>location; i--){
            intArray[i] = intArray[i-1];
        }
        intArray[location] = elem;
        length++;
    }

    /**
     * 删除某个元素
     * @param target 删除的元素
     * @return 成功返回true， 失败返回false
     */
    public boolean delete(int target){
        int index = -1;
        if((index = find(target)) != -1){
            for(int i=index; i<length-1; i++){
                intArray[i] = intArray[i+1];
            }
            length--;
            return true;
        }
        return false;
    }

    public void display() {
        if (length > 0) {
            for(int i=0;i<length;i++){
                System.out.print(intArray[i] + "\t");
            }
            System.out.println();
        }
    }
}
