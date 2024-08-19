package com.company.module.producer;

import com.company.module.event.dto.base.KafkaEvent;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
            CompletableFuture<SendResult<String, String>> future = sendMessage(topic, data, kafkaEvent);
            future.whenComplete((result, ex) -> {
                if (ex == null)
                    log.info("Kafka send Success topic: {}, message: {}, with offset: {}", topic, data, result.getRecordMetadata().offset());
                else
                    log.error("Kafka send Fail topic: {}, message: {}, with cause: {}", topic, data, ex.getCause(), ex);
            });
        } else {
            log.info("Config Kafka topicCode for: {}", topicCode);
        }
    }

    private CompletableFuture<SendResult<String, String>> sendMessage(String topic, String payload, KafkaEvent<?> kafkaEvent) {
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic);
        if (!ObjectUtils.isEmpty(kafkaEvent.getKey()))
            messageBuilder.setHeader(KafkaHeaders.KEY, kafkaEvent.getKey());
        if (!ObjectUtils.isEmpty(kafkaEvent.getHeaders()))
            kafkaEvent.getHeaders().forEach(messageBuilder::setHeader);
        Message<String> message = messageBuilder.build();
        return kafkaTemplate.send(message);
    }

    private CompletableFuture<SendResult<String, String>> sendRecord(String topic, String data, KafkaEvent<?> kafkaEvent) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, kafkaEvent.getKey(), data);
        if (!ObjectUtils.isEmpty(kafkaEvent.getKey()))
            record.headers().add(new RecordHeader(KafkaHeaders.KEY, kafkaEvent.getKey().getBytes()));
        if (!ObjectUtils.isEmpty(kafkaEvent.getHeaders()))
            kafkaEvent.getHeaders().forEach((key, value) -> record.headers().add(new RecordHeader(key, value.getBytes())));
        return kafkaTemplate.send(record);
    }
}
