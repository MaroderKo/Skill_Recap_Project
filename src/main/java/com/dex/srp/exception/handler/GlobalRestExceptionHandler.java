package com.dex.srp.exception.handler;

import com.dex.srp.domain.ErrorResponseDTO;
import com.dex.srp.exception.ApiException;
import com.dex.srp.util.TraceIdProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestExceptionHandler {

    private final ObjectProvider<TraceIdProvider> traceIdProvider;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(ApiException ex, HttpServletRequest request) {
        String traceId = traceIdProvider.getIfAvailable() != null ? traceIdProvider.getIfAvailable().getTraceId() : null;
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(
                new ErrorResponseDTO(
                        ex.getErrorCode().name(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        OffsetDateTime.now(ZoneOffset.UTC),
                        traceId
                )
        );
    }
}
