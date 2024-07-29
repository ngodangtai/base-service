package com.company.module.event.dto.base;

import com.company.module.event.dto.EventDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class RedisEvent<T extends EventDto> extends BaseEvent<T> {
}
