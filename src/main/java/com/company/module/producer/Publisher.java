package com.company.module.producer;

import com.company.module.event.dto.base.BaseEvent;

public interface Publisher {
    Class<?> getType();
    void publish(BaseEvent<?> baseEvent);
}
