package com.house.channel;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/11
 */
public class ChannelDemo {
    public static void main(String[] args) {
        try {
            RandomAccessFile file = new RandomAccessFile("nio-data.txt","rw");
            FileChannel channel = file.getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
