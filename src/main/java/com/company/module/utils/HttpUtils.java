package com.company.module.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.module.exception.ErrorCode;
import com.company.module.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpUtils {
    private static final String LOG1 = "call api: %s, variables: %s -> error: %s";
    private static final String LOG2 = "call api: %s, request: %s, variables: %s -> error: %s";
    private static final String LOG3 = "call api: {}, variables: {} -> response status: {}";
    private static final String LOG4 = "call api: {}, requestBody: {}, uriVariables: {} -> response status: {}";

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private void logInfo(String format, Object... objects) {
        log.info(format, objects);
    }

    private <T> boolean isSuccess(ResponseEntity<T> responseEntity) {
        HttpStatus httpStatus = HttpStatus.valueOf(responseEntity.getStatusCode().value());
        return HttpStatus.Series.SUCCESSFUL.equals(httpStatus.series());
    }

    public <T> T execGet(String url, Class<T> type, Map<String, Object> uriVariables) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, uriVariables);
            logInfo(LOG3, url, uriVariables, responseEntity.getStatusCode());
            return objectMapper.readValue(responseEntity.getBody(), type);
        } catch (Exception e) {
            String errorLog = String.format(LOG1, url, uriVariables, e.getMessage());
            throw new BaseException(e, ErrorCode.UNKNOWN_ERROR, new StringBuilder(errorLog));
        }
    }

    public <T> T execGet(String url, Class<T> type, Object... uriVariables) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, uriVariables);
            logInfo(LOG3, url, uriVariables, responseEntity.getStatusCode());
            return objectMapper.readValue(responseEntity.getBody(), type);
        } catch (Exception e) {
            String errorLog = String.format(LOG1, url, Arrays.toString(uriVariables), e.getMessage());
            throw new BaseException(e, ErrorCode.UNKNOWN_ERROR, new StringBuilder(errorLog));
        }
    }

    public <T, R> T execPost(String url, Class<T> type, R requestBody, Object... uriVariables) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestBody, String.class, uriVariables);
            logInfo(LOG4, url, requestBody, uriVariables, responseEntity.getStatusCode());
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new BaseException(ErrorCode.UNKNOWN_ERROR, new StringBuilder(String.format("call api: %s, request: %s, variables: %s -> not success!", url, requestBody, Arrays.toString(uriVariables))));
            }

            return objectMapper.readValue(responseEntity.getBody(), type);
        } catch (Exception e) {
            String errorLog = String.format(LOG2, url, requestBody, Arrays.toString(uriVariables), e.getMessage());
            throw new BaseException(e, ErrorCode.UNKNOWN_ERROR, new StringBuilder(errorLog));
        }
    }

    public <E> void execPost(String url, E requestBody, Object... uriVariables) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestBody, String.class, uriVariables);
            logInfo(LOG4, url, requestBody, uriVariables, responseEntity.getStatusCode());
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new BaseException(null, ErrorCode.UNKNOWN_ERROR, new StringBuilder(String.format("call api: %s, request: %s, variables: %s -> not success!", url, requestBody, Arrays.toString(uriVariables))));
            }
        } catch (Exception e) {
            String errorLog = String.format(LOG2, url, requestBody, Arrays.toString(uriVariables), e.getMessage());
            throw new BaseException(e, ErrorCode.UNKNOWN_ERROR, new StringBuilder(errorLog));
        }
    }

    public <E> void execPut(String url, E requestBody, Object... uriVariables) {
        try {
            restTemplate.put(url, requestBody, uriVariables);
            logInfo(LOG4, url, requestBody, uriVariables, HttpStatus.OK);
        } catch (Exception e) {
            String errorLog = String.format(LOG2, url, requestBody, Arrays.toString(uriVariables), e.getMessage());
            throw new BaseException(e, ErrorCode.UNKNOWN_ERROR, new StringBuilder(errorLog));
        }
    }

}
