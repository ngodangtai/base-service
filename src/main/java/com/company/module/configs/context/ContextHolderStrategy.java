package com.company.module.configs.context;

import com.company.module.base.dto.Context;

public interface ContextHolderStrategy {

    Context get();

    void set(Context context);

    void clear();

}
