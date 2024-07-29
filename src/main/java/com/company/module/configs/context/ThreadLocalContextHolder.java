package com.company.module.configs.context;

import com.company.module.base.dto.Context;

public class ThreadLocalContextHolder implements ContextHolderStrategy {

    private static final ThreadLocal<Context> contextHolder = new InheritableThreadLocal<>();

    @Override
    public Context get() {
        return contextHolder.get();
    }

    @Override
    public void set(Context context) {
        contextHolder.set(context);
    }

    @Override
    public void clear() {
        contextHolder.remove();
    }

}
