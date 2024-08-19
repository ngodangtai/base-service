package com.company.module.consumer;

import com.company.module.event.dto.AppEventDto;
import com.company.module.event.service.EventHandler;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaComsumer {

    private final EventHandler eventHandler;

    @KafkaListener(topics = "${spring.kafka.topic.test-event}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    @Retryable(retryFor = { Exception.class }, maxAttempts = 4, backoff = @Backoff(delay = 3000L))
    public void appEventDto(AppEventDto appEventDto) {
        eventHandler.handle(appEventDto);
    }

    @KafkaListener(topics = "${spring.kafka.topic.test-event}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    @Retryable(retryFor = { Exception.class }, maxAttempts = 4, backoff = @Backoff(delay = 3000L))
    public void appEventDto(ConsumerRecord<String, String> record,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                            @Header(KafkaHeaders.KEY) String key,
                            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                            @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Received Kafka message: topic={}, key={}, partition={}, offset={}, value={}", topic, key, partition, offset, record.value());
        AppEventDto appEventDto = MapperUtils.readValue(record.value(), AppEventDto.class);
        eventHandler.handle(appEventDto);
    }

}
