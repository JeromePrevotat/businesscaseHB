package com.humanbooster.businesscase.exception;

public class InvalidConfirmationCodeException extends RuntimeException {
    public InvalidConfirmationCodeException(String message) {
        super(message);
    }
}