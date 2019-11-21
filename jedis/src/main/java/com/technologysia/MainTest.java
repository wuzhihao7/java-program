package com.technologysia;

import redis.clients.jedis.*;
import redis.clients.jedis.util.JedisClusterCRC16;

import javax.swing.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class MainTest {
    public static void main(String[] args) throws IOException {
        MainTest client = new MainTest();
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("localhost",7001));
        nodes.add(new HostAndPort("localhost",7002));
        nodes.add(new HostAndPort("localhost",7003));
        nodes.add(new HostAndPort("localhost",7004));
        nodes.add(new HostAndPort("localhost",7005));
        nodes.add(new HostAndPort("localhost",7006));
        String redisPassword = null;
        //测试
        client.jedisCluster(nodes,redisPassword);
        client.clusterPipeline(nodes,redisPassword);
        System.in.read();
    }

    public void jedisCluster(Set<HostAndPort> nodes,String redisPassword) throws UnsupportedEncodingException {
        JedisCluster jc = new JedisCluster(nodes, 2000, 2000,100, redisPassword, new JedisPoolConfig());
        List<String> setKyes = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            setKyes.add("single"+i);
        }
        long start = System.currentTimeMillis();
        for(int j = 0;j < setKyes.size();j++){
            jc.setex(setKyes.get(j),100,"value"+j);
        }
        System.out.println("JedisCluster total time:"+(System.currentTimeMillis() - start));
    }
    //JedisCluster Pipeline 批量写入测试
    public void clusterPipeline(Set<HostAndPort> nodes,String redisPassword) {
        JedisClusterPipeline jedisClusterPipeline = new JedisClusterPipeline(nodes, 2000, 2000,10,redisPassword, new JedisPoolConfig());
        JedisSlotAdvancedConnectionHandler jedisSlotAdvancedConnectionHandler = jedisClusterPipeline.getConnectionHandler();
        Map<JedisPool, List<String>> poolKeys = new HashMap<>();
        List<String> setKyes = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            setKyes.add("pipeline"+i);
        }
        long start = System.currentTimeMillis();
        jedisClusterPipeline.refreshCluster();
        for(int j = 0;j < setKyes.size();j++){
            String key = setKyes.get(j);
            int slot = JedisClusterCRC16.getSlot(key);
            JedisPool jedisPool = jedisSlotAdvancedConnectionHandler.getJedisPoolFromSlot(slot);
            if (poolKeys.keySet().contains(jedisPool)){
                List<String> keys = poolKeys.get(jedisPool);
                keys.add(key);
            }else {
                List<String> keys = new ArrayList<>();
                keys.add(key);
                poolKeys.put(jedisPool, keys);
            }
        }
        //调用Jedis pipeline进行单点批量写入
        for (JedisPool jedisPool : poolKeys.keySet()) {
           try(Jedis jedis = jedisPool.getResource();){
               Pipeline pipeline = jedis.pipelined();
               List<String> keys = poolKeys.get(jedisPool);
               for(int i=0;i<keys.size();i++){
                   pipeline.setex(keys.get(i).getBytes(StandardCharsets.UTF_8),100, FstSerializerUtil.serialize(new Person()));
               }
               pipeline.sync();//同步提交
               Pipeline pipelined = jedis.pipelined();
               List<Response<byte[]>> collect = keys.stream().map(key -> pipeline.get(key.getBytes(StandardCharsets.UTF_8))).collect(Collectors.toList());
               pipeline.sync();
               List<Person> collect1 = collect.stream().map(response -> {
                   byte[] bytes = response.get();
                   return FstSerializerUtil.<Person>deserialize(bytes);
               }).collect(Collectors.toList());
               System.out.println(collect1);
           }catch (Exception e){
               e.printStackTrace();
           }
        }
        System.out.println("JedisCluster Pipeline total time:"+(System.currentTimeMillis() - start));
    }
}
