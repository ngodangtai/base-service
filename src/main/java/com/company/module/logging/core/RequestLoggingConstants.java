package com.company.module.logging.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestLoggingConstants {
    public static final String SUB = "sub";
    public static final String ROLES = "roles";
    public static final String SOTP_SIGN = "sotp_sign";
    public static final String ROLE_RM = "RM";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_CLIENT_IP = "x-client-ip";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String X_API_KEY = "X-Api-Key";
}
