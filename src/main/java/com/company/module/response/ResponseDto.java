package com.company.module.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

    private int status;

    private String code;

    private String message;

    private T data;
    
	public static ResponseDto<String> success() {
		return ResponseDto.success("Success");
	}

	public static <T> ResponseDto<T> success(T data) {
		return ResponseDto.<T>builder()
				.status(HttpStatus.OK.value())
				.code(String.valueOf(HttpStatus.OK.value()))
				.data(data)
				.build();
	}

	public static <T> ResponseDto<T> failed(T data) {
		return ResponseDto.<T>builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.data(data)
				.build();
	}
}
