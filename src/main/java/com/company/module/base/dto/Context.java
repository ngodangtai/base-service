package com.company.module.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Context {

    private SecurityContext securityContext;
}
