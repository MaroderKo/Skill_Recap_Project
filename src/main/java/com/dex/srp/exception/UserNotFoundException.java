package com.dex.srp.exception;

import com.dex.srp.domain.ErrorCode;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(Long userId) {
        super(String.format("User with id %d not found", userId), ErrorCode.ENTITY_NOT_FOUND);
    }
}
