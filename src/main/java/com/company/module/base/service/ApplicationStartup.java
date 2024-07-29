package com.company.module.base.service;

import com.company.module.common.EKey;
import com.company.module.utils.SecurityUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${security.public-key}")
    private String publicKeyPath;
    @Value("${security.private-key}")
    private String privateKeyPath;
    private final CacheViewService cacheViewService;

    @Override
    public void onApplicationEvent(@Nullable ApplicationReadyEvent event) {
        loadKey();
        loadCache();
    }

    private void loadKey() {
        try {
            SecurityUtils.loadPrivateKey(EKey.APP, privateKeyPath);
            SecurityUtils.loadPublicKey(EKey.APP, publicKeyPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void loadCache() {
        try {
            cacheViewService.loadCache();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
