package com.keehoo;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/7
 */
public class TableSizeForDemo {
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static int numberOfLeadingZeros(int i) {
        // HD, Count leading 0's
        if (i <= 0){
            return i == 0 ? 32 : 0;
        }
        int n = 31;
        if (i >= 1 << 16) { n -= 16; i >>>= 16; }
        if (i >= 1 <<  8) { n -=  8; i >>>=  8; }
        if (i >= 1 <<  4) { n -=  4; i >>>=  4; }
        if (i >= 1 <<  2) { n -=  2; i >>>=  2; }
        return n - (i >>> 1);
    }
    static final int tableSizeFor(int cap) {
        int n = -1 >>> numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
        System.out.println(tableSizeFor(-1));
        System.out.println(tableSizeFor(0));
        System.out.println(tableSizeFor(1));
    }
}
