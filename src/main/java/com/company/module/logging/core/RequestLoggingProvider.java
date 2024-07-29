package com.company.module.logging.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class RequestLoggingProvider {

    public RequestLoggingProvider() {
    }

    public static RequestLoggingConfiguration createRequestLoggingConfiguration() {
        ObjectMapper mapper = new YAMLMapper();

        RequestLoggingConfiguration requestLoggingConfiguration;
        try {
            ClassPathResource classPathResource = new ClassPathResource("audit-log.yml");
            requestLoggingConfiguration = mapper.readValue(classPathResource.getInputStream(), RequestLoggingConfiguration.class);
            if (Objects.isNull(requestLoggingConfiguration)) {
                requestLoggingConfiguration = new RequestLoggingConfiguration();
            }
        } catch (RuntimeException | IOException var3) {
            log.error(var3.toString(), var3);
            requestLoggingConfiguration = new RequestLoggingConfiguration();
        }

        requestLoggingConfiguration.postConstruct();
        return requestLoggingConfiguration;
    }
}
