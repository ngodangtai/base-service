package com.company.module;

import com.company.module.configs.RabbitConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan(basePackages = { "com.company.module.entity" })
@EnableJpaRepositories(basePackages = {"com.company.module.repository", "com.company.module.base.repository"})
@EnableJpaAuditing
@ComponentScan(basePackages = { "com.company", "com.company.module", "com.company.module.configs" })
@EnableAspectJAutoProxy
@EnableFeignClients
@Import(value = { RabbitConfig.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
