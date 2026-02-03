package com.dex.srp.exception;

import com.dex.srp.domain.ErrorCode;
import lombok.Getter;

import java.util.Map;

public class ApiException extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;

    @Getter
    private final Map<String, String> details;

    public ApiException(String message, ErrorCode errorCode, Map<String, String> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public ApiException(ErrorCode errorCode, Map<String, String> details) {
        this(errorCode.getDefaultMessage(), errorCode, details);
    }
}
