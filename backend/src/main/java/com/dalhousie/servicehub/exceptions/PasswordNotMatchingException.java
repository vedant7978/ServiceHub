package com.dalhousie.servicehub.exceptions;

public class PasswordNotMatchingException extends RuntimeException {
    public PasswordNotMatchingException(String message) {
        super(message);
    }
}
