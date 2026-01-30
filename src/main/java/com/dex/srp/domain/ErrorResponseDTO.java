package com.dex.srp.domain;

import java.time.OffsetDateTime;

public record ErrorResponseDTO(
        String errorCode,
        String message,
        String path,
        OffsetDateTime timestamp,
        String traceId
) {
}
