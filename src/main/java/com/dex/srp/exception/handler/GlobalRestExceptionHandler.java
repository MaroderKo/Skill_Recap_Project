package com.dex.srp.exception.handler;

import com.dex.srp.domain.ErrorCode;
import com.dex.srp.domain.ErrorResponseDTO;
import com.dex.srp.exception.ApiException;
import com.dex.srp.util.TraceIdProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestExceptionHandler {

    private final ObjectProvider<TraceIdProvider> traceIdProvider;

    private ErrorResponseDTO buildResponse(ErrorCode errorCode, String path, Map<String, String> details) {
        TraceIdProvider localTraceIdProvider = this.traceIdProvider.getIfAvailable();
        String traceId = localTraceIdProvider != null ? localTraceIdProvider.getTraceId() : null;
        return new ErrorResponseDTO(errorCode.name(), errorCode.getDefaultMessage(), path, details, OffsetDateTime.now(ZoneOffset.UTC), traceId);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(ApiException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(buildResponse(ex.getErrorCode(),
                        request.getRequestURI(),
                        ex.getDetails()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(buildResponse(errorCode,
                        request.getRequestURI(),
                        ex.getBindingResult().getFieldErrors().stream()
                                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a.concat(", ").concat(b)))
                )
        );
    }
}
