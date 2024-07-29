package com.company.module.logging;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.company.module.common.Constants;
import com.company.module.jwt.BaseJWT;
import com.company.module.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AutoLoggingServiceImpl implements AutoLoggingService {
	private final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR","X-CLIENT-IP", "X-Client-IP" };

	private final String EMPTY_STRING = "\"\"";

	@Async(Constants.Executor.EXECUTOR_ASYNC)
	@Override
	public void writeAccessLog(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();

		BaseJWT jwt = null;
		HttpServletRequest request = null;
		List<Object> argsForLogging = new ArrayList<>();
		for (Object arg : args) {
			if (arg instanceof BaseJWT) {
				jwt = (BaseJWT) arg;
			} else if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
			} else if (arg instanceof HttpServletResponse) {
				log.warn("Got a response parameter");
			} else {
				try {
					argsForLogging.add(arg == null ? Constants.Character.EMPTY : arg.toString());
				} catch (Exception e){
					log.warn("parameter {}", e.getMessage());
				}
			}
		}

		StringBuilder auditLog = new StringBuilder();
		auditLog.append("Authorization=").append(getAuthorization(jwt)).append(Constants.Character.SPACE);
		auditLog.append("X-API-Key=").append(getXApiKey(jwt)).append(Constants.Character.SPACE);
		auditLog.append("body=").append(standardizeValue(argsForLogging.toString())).append(Constants.Character.SPACE);
		auditLog.append("client=").append(remoteIp(request)).append(Constants.Character.SPACE);
		auditLog.append("response-body=").append(EMPTY_STRING).append(Constants.Character.SPACE);
		auditLog.append("response-status=").append(EMPTY_STRING).append(Constants.Character.SPACE);
		auditLog.append("uri=").append(getUrl(request)).append(Constants.Character.SPACE);
		loggingAccess(jwt,auditLog);


		log.trace(auditLog.toString());
	}
	private void loggingAccess(BaseJWT jwt, StringBuilder auditLog){
		// FIXED: null pointer exception
		if(jwt != null && jwt.isExistJwt()) {
			auditLog.append("SessionID=").append( jwt.getSessionID() ).append(Constants.Character.SPACE);
			auditLog.append("ValidationValue=").append( jwt.getOtp() ).append(Constants.Character.SPACE);
			auditLog.append("ValidationType=").append( jwt.getOtpType()).append(Constants.Character.SPACE);
		}
	}
	private String remoteIp(HttpServletRequest request) {
		if(request == null){
			return EMPTY_STRING;
		} else {
			StringBuilder value = new StringBuilder();
			for (String header : IP_HEADER_CANDIDATES) {
				String ip = request.getHeader(header);
				if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
					value.append(Constants.Character.SPACE).append(header).append(":").append(ip);
				}
			}
			if(StringUtils.isBlank(value.toString())){
				value = new StringBuilder(request.getRemoteAddr());
			}
			return standardizeValue(value.toString());
		}
	}

	private String getUrl(HttpServletRequest req) {
		if(req == null){
			return EMPTY_STRING;
		}
		String reqUrl = req.getRequestURL().toString();
		String queryString = req.getQueryString();
		if (queryString != null) {
			reqUrl += "?"+queryString;
		}
		return standardizeValue(reqUrl);
	}

	private String getAuthorization(BaseJWT jwt){
		if(jwt == null){
			return standardizeValue(Constants.Character.EMPTY);
		}
		String claimValue = EMPTY_STRING;

		try {
			Map<String, Claim> claimMap = jwt.getClaimsFromJwt();
			claimValue = getClaimsValue(claimMap);
		} catch (Exception ignored) {
		}
		return standardizeValue(claimValue);
	}

	private String getXApiKey(BaseJWT jwt){
		if(jwt == null){
			return standardizeValue(Constants.Character.EMPTY);
		}
		String claimValue = EMPTY_STRING;

		try {
			if (!StringUtils.isEmpty(jwt.getApiKey())) {
				DecodedJWT apikey = JWT.decode(jwt.getApiKey());
				claimValue = getClaimsValue(apikey.getClaims());
			}
		} catch (Exception ignored) {
		}
		return standardizeValue(claimValue);
	}

	private String getClaimsValue(Map<String, Claim> claimMap){
		Map<String, String> convertMap = new HashMap<>();
		if(!ObjectUtils.isEmpty(claimMap)){
			for(Map.Entry<String, Claim> entry : claimMap.entrySet()){
				convertMap.put(entry.getKey(), entry.getValue().asString());
			}
		}
		return MapperUtils.writeValueAsString(convertMap);
	}

	private String standardizeValue(String value) {
		if(StringUtils.isEmpty(value)){
			return EMPTY_STRING;
		} else {
			String nonQuoteStr  = value.replace("\"", Constants.Character.EMPTY);
			return "\"" + nonQuoteStr + "\"";
		}
	}
}
