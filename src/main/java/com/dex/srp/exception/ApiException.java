package com.dex.srp.exception;

import com.dex.srp.domain.ErrorCode;
import lombok.Getter;

public class ApiException extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;

    public ApiException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
