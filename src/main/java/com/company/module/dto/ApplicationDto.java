package com.company.module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class ApplicationDto implements Serializable {

    @JsonProperty("id")
    private Long appId;

    @JsonProperty("appName")
    private String appName;

    @JsonProperty("appDesc")
    private String appDesc;

    @JsonProperty("status")
    private Integer status;
}
