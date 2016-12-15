package org.sample;

import org.apache.ignite.*;
import org.apache.ignite.stream.StreamTransformer;
import org.springframework.beans.factory.annotation.Autowired;
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


    // inject the actual template
   /* @Autowired
    private RedisTemplate<String, String> template;
*/
    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    /*@Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
 */
    IgniteDataStreamer<String, Long> stmr;
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    IgniteAtomicLong inventory;

    @PostConstruct
    void init(){
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
        },0, 5, TimeUnit.SECONDS);
    }

    public void addData(String word){
        inventory.incrementAndGet();
    }

    public void read(){


        // Mark this cluster member as client.
        //Ignition.setClientMode(true);

        // Create a streamer to stream words into the cache.
            // Allow data updates.
            // Configure data transformation to count instances of the same word.
            stmr.receiver(StreamTransformer.from((e, arg) -> {
                // Get current count.
                Long val = e.getValue();
                // Increment current count by 1.
                e.setValue(val == null ? 1L : val + 1);
                System.out.println(e.getValue());
                return null;
            }));

            // Stream words from "alice-in-wonderland" book.
            /*int i = 0;
            while (i < 2) {
                // Read words from a text file.
                for (final String word : "Print words using runnable".split(" "))  {
                    if (!word.trim().isEmpty())
                        stmr.addData(word, 1L);
                }
                ++i;
            }*/
    }

}
