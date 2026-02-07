package com.dex.srp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Entity not found"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed");

    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String defaultMessage;

}
