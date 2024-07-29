package com.company.module.logging.core;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    public RequestLoggingFilter() {
    }

    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info(message);
    }

    protected void afterRequest(HttpServletRequest request, String message) {
        log.info(message);
    }

    @PostConstruct
    public void init() {
        try {
            RequestLoggingConfiguration requestLoggingConfiguration = RequestLoggingProvider.createRequestLoggingConfiguration();
            this.setBeforeMessagePrefix(requestLoggingConfiguration.getBeforeMessagePrefix());
            this.setBeforeMessageSuffix(requestLoggingConfiguration.getBeforeMessageSuffix());
            this.setAfterMessagePrefix(requestLoggingConfiguration.getAfterMessagePrefix());
            this.setAfterMessageSuffix(requestLoggingConfiguration.getAfterMessageSuffix());
            this.setIncludeClientInfo(requestLoggingConfiguration.getIncludeClientInfo());
            this.setIncludeHeaders(requestLoggingConfiguration.getIncludeHeaders());
            this.setIncludeQueryString(requestLoggingConfiguration.getIncludeQueryString());
            this.setIncludePayload(requestLoggingConfiguration.getIncludePayload());
            this.setMaxPayloadLength(requestLoggingConfiguration.getMaxPayloadLength());
            RequestLogging[] var2;
            int var3;
            int var4;
            RequestLogging ignoredLogPath;
            HttpMethod[] var6;
            int var7;
            int var8;
            HttpMethod method;
            if (Objects.nonNull(requestLoggingConfiguration.getIncludedPayloadPaths())) {
                var2 = requestLoggingConfiguration.getIncludedPayloadPaths();
                var3 = var2.length;

                for(var4 = 0; var4 < var3; ++var4) {
                    ignoredLogPath = var2[var4];
                    if (StringUtils.isNotBlank(ignoredLogPath.getMethod())) {
                        method = HttpMethod.valueOf(ignoredLogPath.getMethod());
                        this.addIncludedPayloadPath(ignoredLogPath, method);
                    } else {
                        var6 = HttpMethod.values();
                        var7 = var6.length;

                        for(var8 = 0; var8 < var7; ++var8) {
                            method = var6[var8];
                            ignoredLogPath.setMethod(method.name());
                            this.addIncludedPayloadPath(ignoredLogPath, method);
                        }
                    }
                }
            }

            if (Objects.nonNull(requestLoggingConfiguration.getIgnoredPayloadPaths())) {
                var2 = requestLoggingConfiguration.getIgnoredPayloadPaths();
                var3 = var2.length;

                for(var4 = 0; var4 < var3; ++var4) {
                    ignoredLogPath = var2[var4];
                    if (StringUtils.isNotBlank(ignoredLogPath.getMethod())) {
                        method = HttpMethod.valueOf(ignoredLogPath.getMethod());
                        this.addIgnoredPayloadPath(ignoredLogPath, method);
                    } else {
                        var6 = HttpMethod.values();
                        var7 = var6.length;

                        for(var8 = 0; var8 < var7; ++var8) {
                            method = var6[var8];
                            ignoredLogPath.setMethod(method.name());
                            this.addIgnoredPayloadPath(ignoredLogPath, method);
                        }
                    }
                }
            }

            this.setIncludeResponseBody(requestLoggingConfiguration.getIncludeResponseBody());
            this.setIncludeResponseStatus(requestLoggingConfiguration.getIncludeResponseStatus());
            this.setMaxResponseBodyLength(requestLoggingConfiguration.getMaxResponseBodyLength());
            if (Objects.nonNull(requestLoggingConfiguration.getIncludedResponseBodyPaths())) {
                var2 = requestLoggingConfiguration.getIncludedResponseBodyPaths();
                var3 = var2.length;

                for(var4 = 0; var4 < var3; ++var4) {
                    ignoredLogPath = var2[var4];
                    if (StringUtils.isNotBlank(ignoredLogPath.getMethod())) {
                        method = HttpMethod.valueOf(ignoredLogPath.getMethod());
                        this.addIncludedResponseBodyPath(ignoredLogPath, method);
                    } else {
                        var6 = HttpMethod.values();
                        var7 = var6.length;

                        for(var8 = 0; var8 < var7; ++var8) {
                            method = var6[var8];
                            ignoredLogPath.setMethod(method.name());
                            this.addIncludedResponseBodyPath(ignoredLogPath, method);
                        }
                    }
                }
            }

            if (Objects.nonNull(requestLoggingConfiguration.getIgnoredResponseBodyPaths())) {
                var2 = requestLoggingConfiguration.getIgnoredResponseBodyPaths();
                var3 = var2.length;

                for(var4 = 0; var4 < var3; ++var4) {
                    ignoredLogPath = var2[var4];
                    if (StringUtils.isNotBlank(ignoredLogPath.getMethod())) {
                        method = HttpMethod.valueOf(ignoredLogPath.getMethod());
                        this.addIgnoredResponseBodyPath(ignoredLogPath, method);
                    } else {
                        var6 = HttpMethod.values();
                        var7 = var6.length;

                        for(var8 = 0; var8 < var7; ++var8) {
                            method = var6[var8];
                            ignoredLogPath.setMethod(method.name());
                            this.addIgnoredResponseBodyPath(ignoredLogPath, method);
                        }
                    }
                }
            }

            if (Objects.nonNull(requestLoggingConfiguration.getIgnoredTokenAttributes())) {
                this.addIgnoredTokenAttributes(requestLoggingConfiguration.getIgnoredTokenAttributes());
            }

            if (Objects.nonNull(requestLoggingConfiguration.getIgnoredLogPaths())) {
                var2 = requestLoggingConfiguration.getIgnoredLogPaths();
                var3 = var2.length;

                for(var4 = 0; var4 < var3; ++var4) {
                    ignoredLogPath = var2[var4];
                    if (StringUtils.isNotBlank(ignoredLogPath.getMethod())) {
                        method = HttpMethod.valueOf(ignoredLogPath.getMethod());
                        this.addIgnoredLogPath(ignoredLogPath, method);
                    } else {
                        var6 = HttpMethod.values();
                        var7 = var6.length;

                        for(var8 = 0; var8 < var7; ++var8) {
                            method = var6[var8];
                            ignoredLogPath.setMethod(method.name());
                            this.addIgnoredLogPath(ignoredLogPath, method);
                        }
                    }
                }
            }
        } catch (RuntimeException var10) {
            log.error(var10.toString(), var10);
        }

    }
}
