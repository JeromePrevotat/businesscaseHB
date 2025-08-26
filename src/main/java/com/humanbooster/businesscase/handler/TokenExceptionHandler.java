package com.humanbooster.businesscase.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humanbooster.businesscase.exception.AccessTokenExpiredException;
import com.humanbooster.businesscase.exception.InvalidTokenException;
import com.humanbooster.businesscase.exception.RefreshTokenExpiredException;

@RestControllerAdvice
public class TokenExceptionHandler {
    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleAccessTokenExpired(AccessTokenExpiredException ex) {
        Map<String, Object> response = Map.of(
            "error", "ACCESS_TOKEN_EXPIRED",
            "message", "Access Token has expired",
            "action", "REFRESH_TOKEN"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleRefreshTokenExpired(RefreshTokenExpiredException ex) {
        Map<String, Object> response = Map.of(
            "error", "REFRESH_TOKEN_EXPIRED",
            "message", "Refresh Token has expired. Please Login again.",
            "action", "LOGIN"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidToken(InvalidTokenException ex) {
        Map<String, Object> response = Map.of(
            "error", "INVALID_TOKEN",
            "message", "Token is invalid or malformed.",
            "action", "LOGIN"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
