package com.company.module.event.dto.base;

import com.company.module.event.dto.EventDto;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class RabbitEvent<T extends EventDto> extends BaseEvent<T> {
    private Integer priority;
    private Integer delay;
}
