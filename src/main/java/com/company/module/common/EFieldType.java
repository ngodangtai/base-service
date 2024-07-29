package com.company.module.common;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum EFieldType {
    INPUT("INPUT"),
    DROP_LIST("DROP_LIST"),
    REFER("REFER"),
    MAP("MAP"),
    ;

    private final String type;

    public static EFieldType of(String type) {
        for (EFieldType a : EFieldType.values()) {
            if (a.type.equals(type)) {
                return a;
            }
        }
        return null;
    }
}
