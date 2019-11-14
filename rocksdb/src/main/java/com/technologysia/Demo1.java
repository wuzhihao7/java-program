package com.technologysia;

import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Demo1 {
    public static void main(String[] args) {
        // the Options class contains a set of configurable DB options
        // that determines the behaviour of the database.
        try(final Options options = new Options().setCreateIfMissing(true)) {
            // a factory method that returns a RocksDB instance
            try(final RocksDB db = RocksDB.open(options, "rocksdb_data")){
                db.put("hello".getBytes(), "world".getBytes());
                byte[] bytes = db.get("hello".getBytes());
                System.out.println(new String(bytes));
            }
        }catch (RocksDBException e){
            e.printStackTrace();
        }
    }
}
