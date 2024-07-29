package com.company.module.producer;

import com.company.module.event.dto.base.RabbitEvent;
import com.company.module.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitPublisher extends AbstractPublisher<RabbitEvent<?>> {

    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;
    private final RabbitTemplate rabbitTemplate;
    private final Environment environment;

    private static final String ROUTING_KEY_FORMAT = "rabbitmq.queue.%s.key";

    @Override
    public Class<?> getType() {
        return RabbitEvent.class;
    }

    @Override
    protected void send(RabbitEvent<?> rabbitEvent) {
        String routingCode = String.format(ROUTING_KEY_FORMAT, rabbitEvent.getBody().getActionType());
        String routingKey = environment.getProperty(routingCode);
        if (!ObjectUtils.isEmpty(routingKey)) {
            String body = MapperUtils.writeValueAsString(rabbitEvent.getBody());
            MessageProperties properties = MessagePropertiesBuilder.newInstance().setContentType("application/json").build();
            if (!ObjectUtils.isEmpty(rabbitEvent.getPriority())) properties.setPriority(rabbitEvent.getPriority());
            if (!ObjectUtils.isEmpty(rabbitEvent.getDelay())) properties.setDelay(rabbitEvent.getDelay());
            Message message = MessageBuilder.withBody(body.getBytes()).andProperties(properties).build();
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
            log.info("Rabbit send Success routingKey: {}, message: {}", routingKey, body);
        } else {
            log.info("Config Rabbit routingKey for: {}", routingCode);
        }
    }
}
