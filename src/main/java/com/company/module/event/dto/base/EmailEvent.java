package com.company.module.event.dto.base;

import com.company.module.event.dto.AttachmentDto;
import com.company.module.event.dto.EventDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class EmailEvent<T extends EventDto> extends BaseEvent<T> {
    private String subject;
    private String receiver;
    private String cc;
    private String bcc;
    private String content;
    private List<AttachmentDto> fileAttachs;
}
