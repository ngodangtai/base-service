package com.company.module.event.common;

import com.company.module.event.dto.AppEventDto;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum EEventClass {
    APP_ACTION(EventActionType.APP_ACTION, AppEventDto.class),
    ;
    private final String actionType;
    private final Class<?> clazz;
    private static final Map<String, Class<?>> lookup = new HashMap<>();

    static {
        for (EEventClass e : values()) {
            lookup.put(e.actionType, e.clazz);
        }
    }

    public static Class<?> getClass(String actionType) {
        if (lookup.containsKey(actionType)) return lookup.get(actionType);
        else throw new RuntimeException("Not support actionType: " + actionType);
    }
}
