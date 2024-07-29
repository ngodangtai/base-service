package com.company.module.consumer;

import com.company.module.event.dto.EventDto;
import com.company.module.event.common.EEventClass;
import com.company.module.event.service.EventHandler;
import com.company.module.utils.MapperUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisComsumer implements MessageListener {

    private final EventHandler eventHandler;
    private final StringRedisSerializer stringRedisSerializer;

    @Override
    public void onMessage(final @NonNull Message message, final byte[] pattern) {
        EventDto eventDto;
        try {
            log.info("Redis handle message: {}", message);
            JSONObject jsonObject = new JSONObject(stringRedisSerializer.deserialize(message.getBody()));
            String actionType = jsonObject.getString("actionType");
            eventDto = (EventDto) MapperUtils.readValue(jsonObject.toString(), EEventClass.getClass(actionType));
        } catch (Exception ex) {
            log.error("Redis handle error message: {}, cause: {}", message, ex.getCause(), ex);
            throw ex;
        }
        eventHandler.handle(eventDto);
    }
}
