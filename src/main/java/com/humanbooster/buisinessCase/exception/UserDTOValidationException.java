package com.humanbooster.buisinessCase.exception;

import java.util.Map;

public class UserDTOValidationException extends RuntimeException {
    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
