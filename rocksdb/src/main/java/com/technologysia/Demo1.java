package com.technologysia;

import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Demo1 {
    public static void main(String[] args) throws RocksDBException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        final Options options = new Options().setCreateIfMissing(true);
        final RocksDB db = RocksDB.open(options, "rocksdb_data");
        IntStream.range(0, 10000).forEach(i -> {
            executorService.execute(()->{
                try {
                    db.put(("hello"+i).getBytes(), ("world"+i).getBytes());
                    byte[] bytes = db.get(("hello"+i).getBytes());
                    System.out.println(new String(bytes));
                } catch (RocksDBException e) {
                    e.printStackTrace();
                }
            });
        });
        executorService.shutdown();
    }
}