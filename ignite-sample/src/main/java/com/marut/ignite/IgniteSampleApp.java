package com.marut.ignite;

import org.apache.ignite.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

/**
 * Created by marutsingh on 12/15/16.
 */
@SpringBootApplication
public class IgniteSampleApp {

    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(IgniteSampleApp.class, args);
    }

   /* @Bean
    Ignite ignite() {
        igniteSpringBean.
       return Ignition.start();
    }*/

    @Bean(name = "ignite")
    IgniteSpringBean igniteSpringBean(){
        IgniteSpringBean igniteSpringBean = new IgniteSpringBean();
        /*igniteSpringBean.setApplicationContext(applicationContext);
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setIpFinder(new TcpDiscoveryVmIpFinder());
        igniteConfiguration.setDiscoverySpi(discoverySpi);
        igniteSpringBean.setConfiguration(igniteConfiguration);*/
        return igniteSpringBean;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate< String, Long > redisTemplate() {
        final RedisTemplate< String, Long > template =  new RedisTemplate< String, Long >();
        template.setConnectionFactory( jedisConnectionFactory() );
        template.setKeySerializer( new GenericToStringSerializer <String> (String.class) );
        template.setHashValueSerializer( new GenericToStringSerializer< Long >( Long.class ) );
        template.setValueSerializer( new GenericToStringSerializer< Long >( Long.class ) );
        return template;
    }

    @Bean
    RedisMessageListenerContainer messageListenerContainer(MessageListenerAdapter listenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(listenerAdapter,new ChannelTopic("inventory"));
       // container.start();

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    class Receiver {
        private String name;

        @Autowired
        public Receiver() {
        }

        public void receiveMessage(String message) {
            System.out.println(" received <" + message + ">");
            WordReader wordReader = applicationContext.getBean(WordReader.class);
            wordReader.addData(message);
        }
    }
}
