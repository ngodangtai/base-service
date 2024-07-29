package com.company.module.event.action;

import com.company.module.event.dto.EventDto;

public interface EventAction {
    void handle(EventDto eventDto);
}
