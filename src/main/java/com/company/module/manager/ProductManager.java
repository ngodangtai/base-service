package com.company.module.manager;

import com.company.module.base.service.CacheViewService;
import com.company.module.base.view.ApplicationView;
import com.company.module.request.CacheReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductManager extends BaseManager {

    private final CacheViewService redisCacheService;

    public ResponseEntity<Map<String, ApplicationView>> getAllProduct(CacheReq cacheReq) {
        return ResponseEntity.ok(redisCacheService.getApplicationViews());
    }

    public ResponseEntity<ApplicationView> getProduct(CacheReq cacheReq) {
        return ResponseEntity.ok(redisCacheService.getApplicationView(cacheReq.getId()));
    }
}
