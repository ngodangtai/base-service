package com.company.module.configs;

import com.company.module.common.Constants;
import com.company.module.consumer.RedisComsumer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;

@Configuration
@EnableRedisRepositories("com.company.module")
public class RedisConfig {

    @Autowired
    private RedisComsumer redisComsumer;
    @Value("${spring.data.redis.default-topic:defaultTopic}")
    private String defaultTopic;

    @Primary
    @Bean(name = "stringRedisSerializer")
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Primary
    @Bean(name = "jedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory cf,
                                                       StringRedisSerializer stringRedisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Primary
    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory(@Qualifier("redissonClient") RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Primary
    @Bean(name = "redissonClient")
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        return Redisson.create(getConfig(redisProperties));
    }

    private Config getConfig(RedisProperties redisProperties) {
        var config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + Constants.Character.COLON + redisProperties.getPort())
                .setPassword(redisProperties.getPassword())
                .setDatabase(redisProperties.getDatabase())
                .setTimeout((int) redisProperties.getTimeout().toMillis())
                .setConnectTimeout((int) redisProperties.getConnectTimeout().toMillis())
                .setKeepAlive(true);
        return config;
    }

    @Primary
    @Bean(name = "defaultTopic")
    public ChannelTopic topic() {
        return new ChannelTopic(defaultTopic);
    }

    @Primary
    @Bean
    public RedisMessageListenerContainer redisContainer(@Qualifier("redisConnectionFactory") RedisConnectionFactory cf,
                                                        ChannelTopic defaultTopic) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(cf);
        container.addMessageListener(messageListener(), Collections.singleton(defaultTopic));
        return container;
    }

    @Primary
    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(redisComsumer);
    }
}
