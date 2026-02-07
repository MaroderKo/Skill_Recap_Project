package com.dex.srp.exception;

import lombok.Getter;

@Getter
public class DomainValidationException extends RuntimeException {
    private final String field;

    public DomainValidationException(String field, String message) {
        super(message);
        this.field = field;
    }
}
