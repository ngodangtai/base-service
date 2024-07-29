package com.company.module.logging.core;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class RequestLoggingConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final RequestLoggingFilter requestLoggingFilter;

    public RequestLoggingConfigurer(RequestLoggingFilter requestLoggingFilter) {
        this.requestLoggingFilter = requestLoggingFilter;
    }

    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(this.requestLoggingFilter, UsernamePasswordAuthenticationFilter.class);
    }
}