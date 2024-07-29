package com.company.module.event.dto.base;

import com.company.module.event.dto.EventDto;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class InboxEvent<T extends EventDto> extends BaseEvent<T> {
    private String subject;
    private String receiver;
    private String content;
}
