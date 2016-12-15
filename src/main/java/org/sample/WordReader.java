package org.sample;

import org.apache.ignite.*;
import org.apache.ignite.stream.StreamTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by marutsingh on 12/15/16.
 */
@Service
public class WordReader {

   /* @Autowired
    Ignite ignite;
*/

    @Autowired
    IgniteSpringBean ignite;

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    IgniteAtomicLong inventory;

    @Value("${ignite.node.mode.client}")
    Boolean clientMode;

    @PostConstruct
    void init(){
        ignite.getConfiguration().setClientMode(clientMode);
        IgniteCache<String, Long> stmCache = ignite.getOrCreateCache("inventory");
        inventory = ignite.atomicLong(
                "atomicName", // Atomic long name.
                0L,            // Initial value.
                true         // Create if it does not exist.
        );
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Incremented value: " + inventory.get());
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void addData(String word){
        inventory.incrementAndGet();
    }
}
