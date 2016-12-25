package com.marut.ignite;

import org.apache.ignite.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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


    @Autowired
    RedisTemplate<String, Long> redisTemplate;

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    ExecutorService redisAddSimulator = Executors.newFixedThreadPool(4);
    ExecutorService redisNegativeSimulator = Executors.newFixedThreadPool(4);
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
               // System.out.println("Incremented value: " + inventory.get());
            }
        }, 0, 5, TimeUnit.SECONDS);

        redisTemplate.opsForHash().put("a","c",0L);
    }

    void simulatePageStream(){
        for (int i = 1; i < 5; i ++){
            int counter = (i % 2 == 0) ? 1 : -1;
            redisAddSimulator.submit(new Runnable() {
                private volatile long a = 0L;
                @Override
                public void run() {
                    while (a < 1000){
                        redisTemplate.opsForHash().increment("a","c",counter);
                        a++;
                    }
                }
            });
        }

        for (int i = 1; i < 5; i ++){
            redisAddSimulator.submit(new Runnable() {
                private volatile long a = 0L;
                @Override
                public void run() {
                    while (a < 1000){
                        redisTemplate.opsForHash().increment("a","c",-1);
                        a++;
                    }
                }
            });
        }
    }

    public void addData(String word){
        inventory.incrementAndGet();
    }

    public void addCounter(){

    }
}
