package com.keehoo.buffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/24
 */
public class BufferedReaderDemo {
    public static void main(String[] args) {
       try (
            BufferedReader br = Files.newBufferedReader(Paths.get("C:\\job\\workspace\\idea\\java-samples\\nio\\src\\main\\resources\\channel-read.txt"))
        ){
            System.out.println(br.readLine());
       }catch (IOException e){
           e.printStackTrace();
       }
    }
}
