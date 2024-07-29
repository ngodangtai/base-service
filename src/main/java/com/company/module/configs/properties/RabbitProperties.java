package com.company.module.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.springframework.boot.autoconfigure.amqp.RabbitProperties.Listener;

@Data
@Component(value = "rabbitmqProperties")
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String virtualHost;
    private String addresses;
    private Listener listener;
}
