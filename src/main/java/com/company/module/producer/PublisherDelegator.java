package com.company.module.producer;

import com.company.module.configs.context.ContextHolder;
import com.company.module.event.dto.base.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PublisherDelegator {
    private final PublisherFactory publisherFactory;

    public void publish(BaseEvent<?> baseEvent) {
        Publisher publisher = publisherFactory.getInstance(baseEvent.getClass());
        if (publisher != null) {
            baseEvent.setContext(ContextHolder.getContext());
            publisher.publish(baseEvent);
        } else {
            log.info("Undefined publisher by event");
        }
    }
}
