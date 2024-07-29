package com.company.module.configs;

import com.company.module.configs.interceptor.HeaderModifierInterceptor;
import com.company.module.utils.MapperUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.company.module.exception.RestClientErrorHandler;
import com.company.module.utils.DateUtils;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.client.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Configuration
@EnableRetry
public class SystemConfig {

	@Value("${api.key.int}")
	private String apiKey;
	@Value("${api.key.aws:company}")
	private String apiKeyAws;
	@Value("${bond.listener.simple.retry.max-attempts:15}")
	private Integer maxAttempts;
	@Value("${internal.service.connection.timeout:30000}")
	private Long connectionTimeOut;

	@Bean(name = "restTemplateHasTimeout")
	public RestTemplate getRestTemplateHasTimeout(RestTemplateBuilder restTemplateBuilder) {
		RestTemplate restClient = restTemplateBuilder
				.setConnectTimeout(Duration.ofMillis(connectionTimeOut))
				.setReadTimeout(Duration.ofMillis(connectionTimeOut + 5000))
				.build();
		restClient.setMessageConverters(getMessageConverters());
		restClient.setErrorHandler(new RestClientErrorHandler());
		List<ClientHttpRequestInterceptor> interceptors = restClient.getInterceptors();
		interceptors.add(new HeaderModifierInterceptor(apiKey));
		restClient.setInterceptors(interceptors);
		return restClient;
	}

	@Bean("restTemplate")
	@Primary
	public RestTemplate getRestTemplate() {
		return getRestTemplate(apiKey);
	}

	@Bean("restTemplateAws")
	public RestTemplate getRestTemplateAws() {
		return getRestTemplate(apiKeyAws);
	}

	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(2000L);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts);
		retryTemplate.setRetryPolicy(retryPolicy);

		return retryTemplate;
	}

	private RestTemplate getRestTemplate(String apiKey) {
		RestTemplate restClient = new RestTemplate(getClientHttpRequestFactory());
		restClient.setMessageConverters(getMessageConverters());
		restClient.setErrorHandler(new RestClientErrorHandler());
		List<ClientHttpRequestInterceptor> interceptors = restClient.getInterceptors();
		interceptors.add(new HeaderModifierInterceptor(apiKey));
		restClient.setInterceptors(interceptors);
		return restClient;
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory();
	}

	private List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new BufferedImageHttpMessageConverter());
		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(jackson2HtmlConverter());
		return messageConverters;
	}

	/*
	 * Add the Jackson Message converter
	 * Note: here we are making this converter to process any kind of response,
	 * not only application/*json, which is the default behaviour
	 */
	private MappingJackson2HttpMessageConverter jackson2HtmlConverter() {
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		MediaType[] mt = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8,
				MediaType.APPLICATION_OCTET_STREAM, MediaType.MULTIPART_FORM_DATA,
				MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_PLAIN};
		messageConverter.setSupportedMediaTypes(Arrays.asList(mt));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		messageConverter.setObjectMapper(MapperUtils.getObjectMapper());
		return messageConverter;
	}

	@Bean
	public WebClient.Builder getWebClient() {
		WebClient.Builder webClientBuilder = WebClient.builder();
		ReactorClientHttpConnector connector = new ReactorClientHttpConnector();
		webClientBuilder.clientConnector(connector);
		return webClientBuilder;
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> builder.dateFormat(new StdDateFormat());
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		Locale.setDefault(defaultLocale());
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(3600); // Refresh cache once per hour.
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}

	@Bean
	public Locale defaultLocale() {
		return new Locale("vi", "VN");
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
		return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
	}

	@Bean
	public FormattingConversionService conversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.setDateFormatter(DateTimeFormatter.ofPattern(DateUtils.SHORT_TIME_PATTERN));
		registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DateUtils.FULL_TIME_PATTERN));
		registrar.registerFormatters(conversionService);
		return conversionService;
	}

}