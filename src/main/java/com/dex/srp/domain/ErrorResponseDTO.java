package com.dex.srp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public final class ErrorResponseDTO {
    private final String errorCode;
    private final String message;
    private final String path;
    private final Map<String, String> details;
    private final OffsetDateTime timestamp;
    private final String traceId;

}
