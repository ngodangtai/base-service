package com.company.module.configs.interceptor;

import com.company.module.base.dto.Context;
import com.company.module.base.dto.SecurityContext;
import com.company.module.configs.context.ContextHolder;
import com.company.module.jwt.BaseJWT;
import com.company.module.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Service
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {
        var token = StringUtils.hasText(request.getHeader(HttpHeaders.AUTHORIZATION))
                ? request.getHeader(HttpHeaders.AUTHORIZATION)
                : request.getHeader(BaseJWT.API_KEY);
        if (StringUtils.hasText(token)) {
            String subject = Optional.ofNullable(SecurityUtils.extractSecurity(token))
                    .map(c -> c.get(BaseJWT.SUB))
                    .map(Object::toString)
                    .orElse(null);
            ContextHolder.setContext(Context.builder()
                    .securityContext(SecurityContext.builder().subject(subject).build())
                    .build());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        ContextHolder.clearContext();
    }
}
