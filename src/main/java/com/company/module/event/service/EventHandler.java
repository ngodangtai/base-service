package com.company.module.event.service;

import com.company.module.event.dto.EventDto;

public interface EventHandler {
    void handle(EventDto eventDto);
}
