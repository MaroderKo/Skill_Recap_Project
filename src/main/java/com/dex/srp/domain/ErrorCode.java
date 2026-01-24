package com.dex.srp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND);

    @Getter
    private final HttpStatus httpStatus;

}
