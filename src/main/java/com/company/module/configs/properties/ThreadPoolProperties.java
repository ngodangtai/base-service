package com.company.module.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "thread-pool" )
public class ThreadPoolProperties {
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private String threadNamePrefix;
    private ExecutorProperty executor;

    @Data
    public static class ExecutorProperty {
        private ThreadPoolProperties asyncTask;
    }
}
