package com.company.module.configs.interceptor;

import com.company.module.jwt.BaseJWT;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HeaderModifierInterceptor implements ClientHttpRequestInterceptor {
	private final String apiKey;
	private static final String AGENT = "X-User-Agent";
	private static final String AGENT_ID = "Service Base v1.0-Service code 204";

	public HeaderModifierInterceptor(String apiKey) {
		super();
		this.apiKey = apiKey;
	}

	@Override
	public @NonNull ClientHttpResponse intercept(HttpRequest request, byte @NonNull [] body,
												 ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.add(AGENT, AGENT_ID);
		headers.add(BaseJWT.API_KEY, apiKey);
		ClientHttpResponse response = execution.execute(request, body);
		String respBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
		log.info("URI: {}; Request: {}; Response: {}", request.getURI(), new String(body, StandardCharsets.UTF_8), respBody);
		return response;
	}
}