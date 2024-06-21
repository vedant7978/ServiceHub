package com.dalhousie.servicehub.exceptions;

public class BlackListTokenAlreadyExistsException extends RuntimeException {
    public BlackListTokenAlreadyExistsException(String message) {
        super(message);
    }
}
