package com.company.module.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseSearchReq extends BaseReq {

    public abstract Integer getPageSize();
    public abstract Integer getPageNumber();
    public abstract String getOrderBy();
}
