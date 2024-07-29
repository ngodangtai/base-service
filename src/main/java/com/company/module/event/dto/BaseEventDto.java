package com.company.module.event.dto;

import com.company.module.event.common.EventActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseEventDto extends EventDto {

    private String actionType = EventActionType.BASE_ACTION;

}
