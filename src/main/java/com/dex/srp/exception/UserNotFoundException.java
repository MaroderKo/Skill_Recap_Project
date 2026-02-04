package com.dex.srp.exception;

import com.dex.srp.domain.ErrorCode;

import java.util.Map;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(Long userId) {
        super(ErrorCode.ENTITY_NOT_FOUND, Map.of("user_id", String.format("User with id %d not found", userId)));
    }
}
