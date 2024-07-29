package com.company.module.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.company.module.event.common.EventActionType;
import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppEventCopyDto extends EventDto {

    private String actionType = EventActionType.APP_ACTION;

    @JsonProperty("id")
    private Long appId;

    @JsonProperty("appName")
    private String appName;

    @JsonProperty("appDesc")
    private String appDesc;

    @JsonProperty("status")
    private Integer status;
}
