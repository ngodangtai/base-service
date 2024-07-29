package com.company.module.event.dto;

import java.io.Serializable;

public abstract class EventDto implements Serializable {

    public abstract String getActionType();
}
