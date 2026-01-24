package com.dex.srp.domain;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        ErrorCode code,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
