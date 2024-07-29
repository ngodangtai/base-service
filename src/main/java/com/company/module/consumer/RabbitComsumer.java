package com.company.module.consumer;

import com.company.module.event.dto.AppEventDto;
import com.company.module.event.service.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitComsumer {

    private final EventHandler eventHandler;

    @RabbitListener(queues = "${spring.rabbitmq.queue.test-event.name}", containerFactory = "rabbitListenerContainerFactory")
    @Retryable(retryFor = { Exception.class }, maxAttempts = 2, backoff = @Backoff(delay = 3000L))
    public void appEventDto(AppEventDto appEventDto) {
        eventHandler.handle(appEventDto);
    }
}
