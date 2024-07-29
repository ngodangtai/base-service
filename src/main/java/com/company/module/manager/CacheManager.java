package com.company.module.manager;

import com.company.module.base.service.CacheViewService;
import com.company.module.base.view.ApplicationView;
import com.company.module.request.CacheReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheManager extends BaseManager {

    private final CacheViewService redisCacheService;

    public Map<String, ApplicationView> getAllProduct(CacheReq cacheReq) {
        return redisCacheService.getApplicationViews();
    }
}
