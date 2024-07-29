package com.company.module.logging.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

@Getter
@Slf4j
public abstract class AbstractRequestLoggingFilter extends OncePerRequestFilter {
    private boolean includeQueryString = false;
    private boolean includeClientInfo = false;
    private boolean includeHeaders = false;
    private boolean includePayload = false;
    private boolean includeResponseBody = false;
    private boolean includeResponseStatus = true;
    private int maxPayloadLength;
    private int maxResponseBodyLength;
    private String beforeMessagePrefix;
    private String beforeMessageSuffix;
    private String afterMessagePrefix;
    private String afterMessageSuffix;
    private final Map<RequestLogging, HttpMethod> includedPayloadPaths = new HashMap<>();
    private final Map<RequestLogging, HttpMethod> includedResponseBodyPaths = new HashMap<>();
    private final Map<RequestLogging, HttpMethod> ignoredPayloadPaths = new HashMap<>();
    private final Map<RequestLogging, HttpMethod> ignoredResponseBodyPaths = new HashMap<>();
    private final Set<String> ignoredTokenAttributes = new HashSet<>();
    private final Map<RequestLogging, HttpMethod> ignoredLogPaths = new HashMap<>();
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AbstractRequestLoggingFilter() {
    }

    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    private String getClientIp(HttpServletRequest request) {
        String client = request.getHeader("x-client-ip");
        String remoteAddr = request.getRemoteAddr();
        return String.format("%s,%s", client, remoteAddr);
    }

    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !this.isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        HttpServletResponse responseToUse = response;
        boolean shouldLog = this.shouldLog(request);
        if (shouldLog && this.isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request);
        }

        if (shouldLog && (this.isIncludeResponseBody() || this.isIncludeResponseStatus()) && isFirstRequest && !(request instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        if (shouldLog && isFirstRequest) {
            this.beforeRequest(requestToUse, this.getBeforeMessage(requestToUse));
        }

        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            if (shouldLog && !this.isAsyncStarted(requestToUse)) {
                this.afterRequest(requestToUse, this.getAfterMessage(requestToUse, responseToUse));
            }

        }

    }

    private String getBeforeMessage(HttpServletRequest request) {
        return this.createMessage(request, null, this.beforeMessagePrefix, this.beforeMessageSuffix);
    }

    private String getAfterMessage(HttpServletRequest request, HttpServletResponse response) {
        return this.createMessage(request, response, this.afterMessagePrefix, this.afterMessageSuffix);
    }

    protected String createMessage(HttpServletRequest request, HttpServletResponse response, String prefix, String suffix) {
        StringBuilder msg = new StringBuilder();
        msg.append(prefix).append(":");
        msg.append("uri=").append(request.getRequestURI());
        String client;
        if (this.isIncludeQueryString()) {
            client = request.getQueryString();
            if (client != null) {
                msg.append('?').append(client);
            }
        }

        msg.append(";method=").append(request.getMethod());
        if (this.isIncludeClientInfo()) {
            client = this.getClientIp(request);
            if (StringUtils.hasLength(client)) {
                msg.append(";client=").append(client);
            }

            String user = request.getRemoteUser();
            if (user != null) {
                msg.append(";user=").append(user);
            }

            HttpHeaders httpHeaders = (new ServletServerHttpRequest(request)).getHeaders();
            List<String> xForwardedForList = httpHeaders.get("X-Forwarded-For");
            if (Objects.nonNull(xForwardedForList) && !xForwardedForList.isEmpty()) {
                msg.append(";forwardedFor=").append(xForwardedForList.stream().findFirst().get());
            }
        }

        if (this.isIncludeHeaders()) {
            HttpHeaders httpHeaders = (new ServletServerHttpRequest(request)).getHeaders();
            this.parseToken(httpHeaders, "Authorization", msg);
            this.parseToken(httpHeaders, "X-Api-Key", msg);
        }

        byte[] buf;
        int length;
        String responseBody;
        if (this.isIncludePayload() && this.isIncludedPayloadPath(request.getRequestURI(), HttpMethod.valueOf(request.getMethod())) && this.nonIgnoredPayloadPath(request.getRequestURI(), HttpMethod.valueOf(request.getMethod()))) {
            ContentCachingRequestWrapper requestWrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
            if (requestWrapper != null) {
                buf = requestWrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    length = Math.min(buf.length, this.getMaxPayloadLength());

                    try {
                        responseBody = new String(buf, 0, length, requestWrapper.getCharacterEncoding());
                    } catch (UnsupportedEncodingException var13) {
                        responseBody = "[unknown]";
                    }

                    msg.append(";payload=").append(responseBody);
                }
            }
        }

        if (Objects.nonNull(response) && (this.isIncludeResponseBody() || this.isIncludeResponseStatus())) {
            ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
            if (responseWrapper != null) {
                if (this.isIncludeResponseStatus()) {
                    msg.append(";response-status=").append(responseWrapper.getStatus());
                }

                if (this.isIncludeResponseBody() && (responseWrapper.getStatus() != HttpStatus.OK.value() || this.isIncludedResponseBodyPath(request.getRequestURI(), HttpMethod.valueOf(request.getMethod())) && this.nonIgnoredResponseBodyPath(request.getRequestURI(), HttpMethod.valueOf(request.getMethod())))) {
                    buf = responseWrapper.getContentAsByteArray();
                    if (buf.length > 0) {
                        length = Math.min(buf.length, this.getMaxResponseBodyLength());

                        try {
                            responseBody = new String(buf, 0, length, responseWrapper.getCharacterEncoding());
                        } catch (UnsupportedEncodingException var12) {
                            responseBody = "[unknown]";
                        }

                        msg.append(";response-body=").append(responseBody);
                    }
                }

                try {
                    HttpServletResponse rawResponse = (HttpServletResponse) responseWrapper.getResponse();
                    rawResponse.getOutputStream().write(responseWrapper.getContentAsByteArray());
                    rawResponse.flushBuffer();
                } catch (IOException ignored) {
                }
            }
        }

        msg.append(suffix);
        return msg.toString();
    }

    protected boolean shouldLog(HttpServletRequest request) {
        return this.nonIgnoredLogPath(request.getRequestURI(), HttpMethod.valueOf(request.getMethod()));
    }

    public void setMaxResponseBodyLength(int maxResponseBodyLength) {
        Assert.isTrue(maxResponseBodyLength >= 0, "'maxPayloadLength' should be larger than or equal to 0");
        this.maxResponseBodyLength = maxResponseBodyLength;
    }

    public void setMaxPayloadLength(int maxPayloadLength) {
        Assert.isTrue(maxPayloadLength >= 0, "'maxPayloadLength' should be larger than or equal to 0");
        this.maxPayloadLength = maxPayloadLength;
    }

    public void addIncludedPayloadPath(RequestLogging loggingRequest, HttpMethod method) {
        this.includedPayloadPaths.put(loggingRequest, method);
    }

    public void addIncludedResponseBodyPath(RequestLogging loggingRequest, HttpMethod method) {
        this.includedResponseBodyPaths.put(loggingRequest, method);
    }

    protected boolean isIncludedPayloadPath(String path, HttpMethod method) {
        return this.isMatchPath(this.includedPayloadPaths, path, method);
    }

    protected boolean isIncludedResponseBodyPath(String path, HttpMethod method) {
        return this.isMatchPath(this.includedResponseBodyPaths, path, method);
    }

    public void addIgnoredPayloadPath(RequestLogging loggingRequest, HttpMethod method) {
        this.ignoredPayloadPaths.put(loggingRequest, method);
    }

    public void addIgnoredResponseBodyPath(RequestLogging loggingRequest, HttpMethod method) {
        this.ignoredResponseBodyPaths.put(loggingRequest, method);
    }

    public void addIgnoredTokenAttributes(String[] attributes) {
        this.ignoredTokenAttributes.addAll(Arrays.stream(attributes).collect(Collectors.toSet()));
    }

    public void addIgnoredLogPath(RequestLogging loggingRequest, HttpMethod method) {
        this.ignoredLogPaths.put(loggingRequest, method);
    }

    protected boolean nonIgnoredPayloadPath(String path, HttpMethod method) {
        return !this.isMatchPath(this.ignoredPayloadPaths, path, method);
    }

    protected boolean nonIgnoredResponseBodyPath(String path, HttpMethod method) {
        return !this.isMatchPath(this.ignoredResponseBodyPaths, path, method);
    }

    protected boolean nonIgnoredLogPath(String path, HttpMethod method) {
        return !this.isMatchPath(this.ignoredLogPaths, path, method);
    }

    private boolean isMatchPath(Map<RequestLogging, HttpMethod> map, String path, HttpMethod method) {
        Iterator<Map.Entry<RequestLogging, HttpMethod>> var4 = map.entrySet().iterator();

        RequestLogging loggingRequest;
        HttpMethod value;
        do {
            if (!var4.hasNext()) {
                return false;
            }
            Map.Entry<RequestLogging, HttpMethod> entry = var4.next();
            loggingRequest = entry.getKey();
            value = entry.getValue();
        } while (!this.antPathMatcher.match(loggingRequest.getPath(), path) || !method.equals(value));

        return true;
    }

    private void parseToken(HttpHeaders httpHeaders, String headerKey, StringBuilder msg) {
        msg.append(";").append(headerKey).append("=[");
        List<String> authorizationTokens = httpHeaders.get(headerKey);
        if (Objects.nonNull(authorizationTokens) && !authorizationTokens.isEmpty()) {
            String normalizeHeader = normalizeBearer(authorizationTokens.stream().findFirst().get());
            getJwtClaims(normalizeHeader).ifPresent(claims -> claims.forEach((key, value) -> {
                if (!this.ignoredTokenAttributes.contains(key)) {
                    String val = "roles".equals(key) ? Arrays.toString(value.asArray(String.class)) : value.asString();
                    msg.append(key).append(":").append(val).append(",");
                }
            }));
        }
        msg.append("]");
    }

    protected abstract void beforeRequest(HttpServletRequest var1, String var2);

    protected abstract void afterRequest(HttpServletRequest var1, String var2);

    public static Optional<Map<String, Claim>> getJwtClaims(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return Optional.ofNullable(jwt.getClaims());
        } catch (JWTDecodeException var2) {
            log.error("token invalid - error: {}", var2.toString(), var2);
            return Optional.empty();
        }
    }

    public static String normalizeBearer(String authorValue) {
        String regex = String.format("(%s)\\s(.*)", "Bearer");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(authorValue);
        return matcher.find() ? matcher.group(2) : authorValue;
    }

    public void setIncludeQueryString(boolean includeQueryString) {
        this.includeQueryString = includeQueryString;
    }

    public void setIncludeClientInfo(boolean includeClientInfo) {
        this.includeClientInfo = includeClientInfo;
    }

    public void setIncludeHeaders(boolean includeHeaders) {
        this.includeHeaders = includeHeaders;
    }

    public void setIncludePayload(boolean includePayload) {
        this.includePayload = includePayload;
    }

    public void setIncludeResponseBody(boolean includeResponseBody) {
        this.includeResponseBody = includeResponseBody;
    }

    public void setIncludeResponseStatus(boolean includeResponseStatus) {
        this.includeResponseStatus = includeResponseStatus;
    }

    public void setBeforeMessagePrefix(String beforeMessagePrefix) {
        this.beforeMessagePrefix = beforeMessagePrefix;
    }

    public void setBeforeMessageSuffix(String beforeMessageSuffix) {
        this.beforeMessageSuffix = beforeMessageSuffix;
    }

    public void setAfterMessagePrefix(String afterMessagePrefix) {
        this.afterMessagePrefix = afterMessagePrefix;
    }

    public void setAfterMessageSuffix(String afterMessageSuffix) {
        this.afterMessageSuffix = afterMessageSuffix;
    }
}
