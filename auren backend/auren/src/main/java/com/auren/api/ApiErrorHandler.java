package com.auren.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> onError(Exception e) {
        return ResponseEntity.status(500).body(Map.of(
                "status", 500,
                "message", e.getMessage(),
                "details", null
        ));
    }
}
