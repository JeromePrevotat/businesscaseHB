package com.humanbooster.buisinessCase.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humanbooster.buisinessCase.exception.InvalidConfirmationCodeException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setMessage("Bad Request");
        Map<String, String> errorsMap = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(
            fieldError -> fieldError.getField(),
            fieldError -> fieldError.getDefaultMessage()
            ));
        response.setErrors(errorsMap);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidConfirmationCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidConfirmationCode(InvalidConfirmationCodeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "INVALID_CONFIRMATION_CODE");
        error.put("message", ex.getMessage());
        return error;
    }
}
