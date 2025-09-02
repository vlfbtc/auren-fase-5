package com.auren.exception;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCred(BadCredentialsException ex) {
        return error(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String,String> fields = new LinkedHashMap<>();
        for (var f : ex.getBindingResult().getAllErrors()) {
            String field = f instanceof FieldError fe ? fe.getField() : f.getObjectName();
            fields.put(field, f.getDefaultMessage());
        }
        return error(HttpStatus.BAD_REQUEST, "Validation error", fields);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado", null);
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String message, Map<String,String> details) {
        return ResponseEntity.status(status).body(new ApiError(status.value(), message, details));
    }

    @Data @AllArgsConstructor
    static class ApiError {
        private int status;
        private String message;
        private Map<String,String> details;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        private OffsetDateTime timestamp = OffsetDateTime.now();

        public ApiError(int status, String message, Map<String,String> details) {
            this.status = status;
            this.message = message;
            this.details = details;
            this.timestamp = OffsetDateTime.now();
        }
    }
}
