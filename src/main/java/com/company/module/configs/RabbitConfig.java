package com.company.module.configs;

import com.company.module.configs.properties.RabbitProperties;
import com.company.module.utils.MapperUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@EnableRabbit
@Configuration
@Slf4j
public class RabbitConfig {

	private final RabbitProperties rabbitProperties;

	public RabbitConfig(@Qualifier(value = "rabbitmqProperties") RabbitProperties rabbitProperties) {
		this.rabbitProperties = rabbitProperties;
	}

	@Bean(name = "rabbitTemplate")
	@Primary
	public RabbitTemplate rabbitTemplate(@Qualifier(value = "connectionFactory") ConnectionFactory connectionFactory,
										 MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setRetryTemplate(retryTemplate());
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}

	private RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(1000);
		backOffPolicy.setMultiplier(2.0);
		backOffPolicy.setMaxInterval(10000);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		return retryTemplate;
	}

	@Bean(name = "connectionFactory")
	@Primary
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory cf = new CachingConnectionFactory();
		cf.setHost(rabbitProperties.getHost());
		cf.setPort(rabbitProperties.getPort());
		cf.setUsername(rabbitProperties.getUsername());
		cf.setPassword(rabbitProperties.getPassword());
		cf.setVirtualHost(rabbitProperties.getVirtualHost());
		cf.setPublisherReturns(true);
		cf.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
		cf.addConnectionListener(new ConnectionListener() {
			public void onCreate(@NonNull Connection connection) {
				log.info("RabbitMQ onCreate");
			}

			public void onClose(@NonNull Connection connection) {
				log.info("RabbitMQ onClose");
			}
		});
		return cf;
	}

	@Bean(name = "rabbitListenerContainerFactory")
	@Primary
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			@Qualifier(value = "connectionFactory") ConnectionFactory connectionFactory,
			MessageConverter messageConverter) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter);
		factory.setMaxConcurrentConsumers(rabbitProperties.getListener().getSimple().getMaxConcurrency());
		factory.setDefaultRequeueRejected(false);
		return factory;
	}

	@Bean(name = "messageConverter")
	public MessageConverter messageConverter() {
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(MapperUtils.getObjectMapper());
		converter.setCreateMessageIds(true);
		return converter;
	}
}