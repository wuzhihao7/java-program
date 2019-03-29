package com.keehoo.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.stream.IntStream;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/24
 */
public class ChannelTransferDemo {
    public static void main(String[] args) {
        //path of input files
        String filePath = "C:\\job\\workspace\\idea\\java-samples\\nio\\src\\main\\resources\\";
        String[] inputFiles = {filePath + "input1.txt", filePath + "input2.txt", filePath + "input3.txt", filePath + "input4.txt"};
        //path of output file and contents will be written in this file
        String outputFile = filePath + "outputall.txt";
        //acquired the channel for output file
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             //get the channel for input files
             FileChannel targetChannel = fos.getChannel()) {
            IntStream.range(0, inputFiles.length).forEach(i -> {
                try (FileInputStream fis = new FileInputStream(inputFiles[i]);
                     FileChannel inputChannel = fis.getChannel()) {
                    //the data is transfer from input channel to output channel
                    inputChannel.transferTo(0, inputChannel.size(), targetChannel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    System.out.println("all jobs done.");
    }
}
