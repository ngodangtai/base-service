package com.company.module.logging.core;

import java.util.Objects;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class RequestLoggingConfiguration {
    private Boolean includeQueryString;
    private Boolean includeClientInfo;
    private Boolean includeHeaders;
    private Boolean includePayload;
    private Boolean includeResponseBody;
    private Boolean includeResponseStatus;
    private Integer maxPayloadLength;
    private Integer maxResponseBodyLength;
    private String beforeMessagePrefix;
    private String beforeMessageSuffix;
    private String afterMessagePrefix;
    private String afterMessageSuffix;
    private RequestLogging[] includedPayloadPaths;
    private RequestLogging[] includedResponseBodyPaths;
    private RequestLogging[] ignoredPayloadPaths;
    private RequestLogging[] ignoredResponseBodyPaths;
    private String[] ignoredTokenAttributes;
    private RequestLogging[] ignoredLogPaths;

    public RequestLoggingConfiguration() {
    }

    public void postConstruct() {
        if (Objects.isNull(this.includeQueryString)) {
            this.includeQueryString = Boolean.TRUE;
        }
        if (Objects.isNull(this.includeClientInfo)) {
            this.includeClientInfo = Boolean.TRUE;
        }
        if (Objects.isNull(this.includeHeaders)) {
            this.includeHeaders = Boolean.TRUE;
        }
        if (Objects.isNull(this.includePayload)) {
            this.includePayload = Boolean.FALSE;
        }
        if (Objects.isNull(this.includeResponseBody)) {
            this.includeResponseBody = Boolean.FALSE;
        }
        if (Objects.isNull(this.includeResponseStatus)) {
            this.includeResponseStatus = Boolean.TRUE;
        }
        if (Objects.isNull(this.maxPayloadLength)) {
            this.maxPayloadLength = 10000;
        }
        if (Objects.isNull(this.maxResponseBodyLength)) {
            this.maxResponseBodyLength = 10000;
        }
        if (Objects.isNull(this.beforeMessagePrefix)) {
            this.beforeMessagePrefix = "Before request";
        }
        if (Objects.isNull(this.beforeMessageSuffix)) {
            this.beforeMessageSuffix = "";
        }
        if (Objects.isNull(this.afterMessagePrefix)) {
            this.afterMessagePrefix = "After request";
        }
        if (Objects.isNull(this.afterMessageSuffix)) {
            this.afterMessageSuffix = "";
        }
    }
}

