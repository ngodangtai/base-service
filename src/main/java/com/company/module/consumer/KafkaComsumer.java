package com.company.module.consumer;

import com.company.module.event.dto.AppEventDto;
import com.company.module.event.service.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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
}
