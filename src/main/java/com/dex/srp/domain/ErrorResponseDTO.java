package com.dex.srp.domain;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String code,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
