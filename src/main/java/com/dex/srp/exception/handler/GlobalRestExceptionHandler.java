package com.dex.srp.exception.handler;

import com.dex.srp.domain.ErrorCode;
import com.dex.srp.domain.ErrorResponseDTO;
import com.dex.srp.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDTO(
                        ErrorCode.ENTITY_NOT_FOUND,
                        ex.getMessage(),
                        request.getServletPath(),
                        LocalDateTime.now()
                )
        );
    }
}
