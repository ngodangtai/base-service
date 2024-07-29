package com.company.module.event.dto.base;

import com.company.module.base.dto.Context;
import com.company.module.event.dto.EventDto;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseEvent<T extends EventDto> implements Serializable {
    private Context context;
    private T body;
}
