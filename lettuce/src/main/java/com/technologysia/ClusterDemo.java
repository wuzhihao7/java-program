package com.technologysia;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

import java.text.MessageFormat;

public class ClusterDemo {
    public static void main(String[] args) {
        RedisURI uri = RedisURI.builder().withHost("127.0.0.1").build();
        RedisClusterClient redisClusterClient = RedisClusterClient.create(uri);
        StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect();
        RedisAdvancedClusterCommands<String, String> commands = connection.sync();
        commands.setex("name",10, "throwable");
        String value = commands.get("name");
        System.out.println(MessageFormat.format("Get value:{}", value));
    }
}
