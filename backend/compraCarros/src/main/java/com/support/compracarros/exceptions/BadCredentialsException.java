package com.support.compracarros.exceptions;

import com.support.compracarros.models.FieldErrors;

public class BadCredentialsException extends RuntimeException {

    private FieldErrors fieldErrors;

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String message, FieldErrors fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public FieldErrors getFieldErrors() {
        return fieldErrors;
    }
}
