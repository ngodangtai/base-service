package com.company.module.configs.client;
import com.company.module.jwt.BaseJWT;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ThirdPartyInterceptor implements RequestInterceptor {

    @Value("${api.key.3rd}")
    private String xApiKey;

    @Override
    public void apply(RequestTemplate template) {
        template.header(BaseJWT.API_KEY, xApiKey);
        template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }
}