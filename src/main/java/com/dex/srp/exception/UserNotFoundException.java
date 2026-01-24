package com.dex.srp.exception;

import com.dex.srp.domain.ErrorCode;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
