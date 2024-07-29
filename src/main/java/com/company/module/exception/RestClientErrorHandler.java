package com.company.module.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RestClientErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().isError();
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		try {
			log.info("------- Error when call service (Response Code): " + response.getRawStatusCode());
			String errorBody = new BufferedReader(new InputStreamReader(response.getBody())).lines().collect(Collectors.joining("\n"));
			log.info("------- Error when call service (Response Payload): " + errorBody);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
}
