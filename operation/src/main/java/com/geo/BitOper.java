package com.geo;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/13
 */
public class BitOper {
    public static void main(String[] args) {
        short a = (short)0x1234;
        System.out.println("十进制：");
        System.out.println(a);
        System.out.println("二进制：");
        System.out.println(Integer.toBinaryString(a));
        System.out.println("八进制：");
        System.out.println(Integer.toOctalString(a));
        System.out.println(String.format("%o", a));
        System.out.println("十六进制：");
        System.out.println(Integer.toHexString(a));
        System.out.println(String.format("%x", a));
        short b = (short)((a<<8)&0xFF00);
        System.out.println(Integer.toBinaryString(a << 8));
        System.out.println(Integer.toBinaryString(b));
        short c = (short)((a>>8)&0x00FF);

        short d = (short)(b|c);

        System.out.format("%x",d);
    }
}
