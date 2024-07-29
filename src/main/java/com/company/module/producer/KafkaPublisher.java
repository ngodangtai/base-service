package com.company.module.producer;

import com.company.module.event.dto.base.KafkaEvent;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaPublisher extends AbstractPublisher<KafkaEvent<?>> {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Environment environment;
    private static final String TOPIC_CODE_FORMAT = "kafka.topic.%s";

    @Override
    public Class<?> getType() {
        return KafkaEvent.class;
    }

    @Override
    protected void send(KafkaEvent<?> kafkaEvent) {
        String topicCode = String.format(TOPIC_CODE_FORMAT, kafkaEvent.getBody().getActionType());
        String topic = environment.getProperty(topicCode);
        if (!ObjectUtils.isEmpty(topic)) {
            String data = MapperUtils.writeValueAsString(kafkaEvent.getBody());
            CompletableFuture<SendResult<String, String>> future = ObjectUtils.isEmpty(kafkaEvent.getKey())
                    ? kafkaTemplate.send(topic, data) : kafkaTemplate.send(topic, kafkaEvent.getKey(), data);
            future.whenComplete((result, ex) -> {
                if (ex == null)
                    log.info("Kafka send Success topic: {}, message: {}, with offset: {}", topic, data, result.getRecordMetadata().offset());
                else
                    log.info("Kafka send Fail topic: {}, message: {}, with cause: {}", topic, data, ex.getCause(), ex);
            });
        } else {
            log.info("Config Kafka topicCode for: {}", topicCode);
        }
    }
}
