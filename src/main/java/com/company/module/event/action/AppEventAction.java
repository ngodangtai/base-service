package com.company.module.event.action;

import com.company.module.event.dto.AppEventDto;
import com.company.module.event.dto.EventDto;
import com.company.module.utils.MapperUtils;
import com.company.module.event.common.EventActionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = EventActionType.APP_ACTION)
public class AppEventAction extends AbstractEventAction {

    @Override
    public void handle(EventDto eventDto) {
        AppEventDto appEventDto = (AppEventDto) eventDto;
        log.info("Handle success appEventDto: {}", MapperUtils.writeValueAsString(appEventDto));
    }
}
