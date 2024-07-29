package com.company.module.configs.context;

import com.company.module.base.dto.Context;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextHolder {

    private static final ContextHolderStrategy strategy = new ThreadLocalContextHolder();

    public static void clearContext() {
        strategy.clear();
    }

    public static Context getContext() {
        return strategy.get();
    }

    public static void setContext(Context context) {
        strategy.set(context);
    }

}
