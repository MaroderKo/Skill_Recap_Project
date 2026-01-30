package com.dex.srp.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        String errorCode,
        String message,
        String path,
        Map<String, Object> details,
        OffsetDateTime timestamp,
        String traceId
) {
}
