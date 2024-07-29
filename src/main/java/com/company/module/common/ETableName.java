package com.company.module.common;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum ETableName {
    TABLE_APPLICATION(TableNameConstants.TABLE_APPLICATION),
    ;
    private final String value;
}
