package com.company.module.manager;

import com.company.module.dto.ApplicationDto;
import com.company.module.event.dto.AppEventCopyDto;
import com.company.module.event.dto.EventDto;
import com.company.module.event.dto.base.KafkaEvent;
import com.company.module.event.dto.base.RabbitEvent;
import com.company.module.event.dto.base.RedisEvent;
import com.company.module.producer.PublisherDelegator;
import com.company.module.request.ApplicationSearchReq;
import com.company.module.request.BizReq;
import com.company.module.service.table.ApplicationSearchService;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class BizManager extends BaseManager {

    private final PublisherDelegator publisherDelegator;
    private final ApplicationSearchService applicationSearchService;

    public void publishRabbit(BizReq bizReq) {
        applicationSearchService.getApplications().forEach(a -> {
            RabbitEvent<EventDto> rabbitEvent = RabbitEvent.builder().build();
            rabbitEvent.setBody(MapperUtils.copyProperties(a, AppEventCopyDto.class));
            publisherDelegator.publish(rabbitEvent);
        });
    }

    public void publishKafka(BizReq bizReq) {
        applicationSearchService.getApplications().forEach(a -> {
            KafkaEvent<EventDto> kafkaEvent = KafkaEvent.builder().build();
            kafkaEvent.setBody(MapperUtils.copyProperties(a, AppEventCopyDto.class));
            publisherDelegator.publish(kafkaEvent);
        });
    }

    public void publishRedis(BizReq bizReq) {
        ApplicationSearchReq request = ApplicationSearchReq.builder()
                .appIds(Collections.singletonList(1L))
                .build();
        ApplicationDto a = applicationSearchService.searchApplication(request).getContent().get(0);
        RedisEvent<EventDto> redisEvent = RedisEvent.builder().build();
        redisEvent.setBody(MapperUtils.copyProperties(a, AppEventCopyDto.class));
        publisherDelegator.publish(redisEvent);
    }
}
