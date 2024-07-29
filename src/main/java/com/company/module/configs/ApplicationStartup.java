package com.company.module.configs;

import com.company.module.base.repository.S3Repository;
import com.company.module.common.EKey;
import com.company.module.utils.SecurityUtils;
import lombok.*;
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
    private final S3Repository s3Repository;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        try {
            SecurityUtils.loadPrivateKey(EKey.APP, privateKeyPath);
            SecurityUtils.loadPublicKey(EKey.APP, publicKeyPath);
            try {
                if (!s3Repository.checkConnect()) {
                    log.warn("S3 Bucket not Exist !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            } catch (Exception ex) {
                log.error("Connect S3 ERROR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", ex);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
