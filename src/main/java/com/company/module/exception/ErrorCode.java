package com.company.module.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.slf4j.event.Level;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ErrorCode {

	UNKNOWN_ERROR("000", "unknown.error"),
	INVALID_PARAMS("001", "invalid.params", HttpServletResponse.SC_BAD_REQUEST),
	DATA_NOT_FOUND("002", "data.not.found", HttpServletResponse.SC_NOT_FOUND),
	FORM_ERROR("003", "form.error"),
	REST_ERROR("004", "rest.error"),
	PROCESSING_APIKEY_ERROR("005", "processing.apikey.error"),
	PROCESSING_JWT_ERROR("006", "processing.jwt.error"),
	SOURCE_INVALID("007", "load.private.key.error", HttpServletResponse.SC_NOT_FOUND),
	INTERNAL_SERVER_ERROR("008", "internal.server.error", HttpServletResponse.SC_BAD_REQUEST),
	LOAD_PRIVATE_KEY_ERROR("082", "load.private.key.error", HttpServletResponse.SC_NOT_FOUND),
	LOAD_PUBLIC_KEY_ERROR("083", "load.public.key.error", HttpServletResponse.SC_NOT_FOUND),
	GENERATE_KEYPAIR_ERROR("084", "gen.keypair.error", HttpServletResponse.SC_BAD_REQUEST),
	LOAD_KEYPAIR_ERROR("085", "load.keypair.error", HttpServletResponse.SC_NOT_FOUND),
	SAVE_KEYPAIR_ERROR("086", "save.keypair.error", HttpServletResponse.SC_BAD_REQUEST),
	RSA_ENCRYPT_ERROR("087", "rsa.encrypt.error", HttpServletResponse.SC_BAD_REQUEST),
	RSA_DECRYPT_ERROR("088", "rsa.decrypt.error", HttpServletResponse.SC_BAD_REQUEST),
	;

	private static final Map<String, ErrorCode> lookup = new HashMap<String, ErrorCode>();
	static {
		for (ErrorCode e : EnumSet.allOf(ErrorCode.class))
			lookup.put(e.getCode(), e);
	}

	private final String code;
	private final String messageCode;
	private final Integer httpStatus;
	private final Level level;

	ErrorCode(String errorCode, String messageCode) {
		this.code = errorCode;
		this.messageCode = messageCode;
		this.httpStatus = HttpServletResponse.SC_BAD_REQUEST;
		this.level = Level.ERROR;
	}

	ErrorCode(String errorCode, String messageCode, Integer httpStatus) {
		this.code = errorCode;
		this.messageCode = messageCode;
		this.httpStatus = httpStatus;
		this.level = Level.ERROR;
	}

	ErrorCode(String errorCode, String messageCode, Integer httpStatus, Level level) {
		this.code = errorCode;
		this.messageCode = messageCode;
		this.httpStatus = httpStatus;
		this.level = level;
	}

	public static ErrorCode get(String errorCode) {
		return lookup.get(errorCode);
	}

	public String getMsg() {
		return null;
	}

}
