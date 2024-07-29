package com.company.module.base.service;

import com.company.module.common.ETableName;
import com.company.module.exception.BaseException;
import com.company.module.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TableFactory {

    private final ApplicationContext context;

    public TableService<?> getInstance(ETableName tableName) {
        if (tableName == null)
            throw new BaseException(ErrorCode.SOURCE_INVALID, new StringBuilder("TableName invalid"));
        else
            return context.getBean(tableName.getValue(), TableService.class);
    }
}
