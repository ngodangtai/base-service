package com.company.module.exception;

import lombok.Getter;

public class BaseException extends RuntimeException {
    @Getter
    private ErrorCode errorCode;
    private Exception exception;

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(Exception ex, ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.exception = ex;
        this.errorCode = errorCode;
    }

    public BaseException(Exception ex, ErrorCode errorCode, String message) {
        super(message);
        this.exception = ex;
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, StringBuilder message) {
        super(message.toString());
        this.errorCode = errorCode;
    }

    public BaseException(Exception ex, ErrorCode errorCode, StringBuilder message) {
        super(message.toString());
        this.exception = ex;
        this.errorCode = errorCode;
    }

}
