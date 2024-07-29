package com.company.module.request;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ApplicationSearchReq extends BaseSearchReq {

    private Integer pageSize;
    private Integer pageNumber;
    private String orderBy;
    private List<Long> appIds;
    private String appName;
    private String appDesc;
    private Integer status;
}
