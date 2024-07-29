package com.company.module.request;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class CacheReq extends BaseReq {
    private Long id;
}
