package com.humanbooster.buisinessCase.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humanbooster.buisinessCase.exception.UserValidationException;
import com.humanbooster.buisinessCase.exception.ValidationErrorResponse;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleUserValidationException(UserValidationException ex) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setMessage("Registration failed");
        response.setErrors(ex.getErrors());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
