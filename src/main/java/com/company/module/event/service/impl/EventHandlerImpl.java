package com.company.module.event.service.impl;

import com.company.module.event.dto.EventDto;
import com.company.module.event.action.EventAction;
import com.company.module.event.service.EventHandler;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "eventHandler")
@RequiredArgsConstructor
public class EventHandlerImpl implements EventHandler {

    private final ApplicationContext applicationcontext;

    public void handle(EventDto eventDto) {
        try {
            log.info("Handle eventDto: {}", MapperUtils.writeValueAsString(eventDto));
            applicationcontext.getBean(eventDto.getActionType(), EventAction.class).handle(eventDto);
        } catch (Exception ex) {
            log.error("Handle error eventDto: {}, cause: {}", MapperUtils.writeValueAsString(eventDto), ex.getCause(), ex);
            throw ex;
        }
    }
}
