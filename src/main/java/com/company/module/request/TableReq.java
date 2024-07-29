package com.company.module.request;

import com.company.module.base.dto.TableDto;
import com.company.module.common.ETableName;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TableReq extends BaseReq {
    private ETableName tableName;
    private TableDto tableDto;
}
