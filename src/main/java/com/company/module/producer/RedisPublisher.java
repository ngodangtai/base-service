package com.company.module.producer;

import com.company.module.event.dto.base.RedisEvent;
import com.company.module.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisPublisher extends AbstractPublisher<RedisEvent<?>> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public RedisPublisher(@Qualifier(value = "jedisTemplate") RedisTemplate<String, Object> redisTemplate,
                          ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public Class<?> getType() {
        return RedisEvent.class;
    }

    @Override
    protected void send(RedisEvent<?> redisEvent) {
        redisTemplate.convertAndSend(topic.getTopic(), redisEvent.getBody());
        log.info("Redis send Success message: {}", MapperUtils.writeValueAsString(redisEvent.getBody()));
    }
}
