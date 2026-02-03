package com.dex.srp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public final class ErrorResponseDTO {
    private final String errorCode;
    private final String message;
    private final String path;
    private final Map<String, String> details;
    private final OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
    private final String traceId;

}
