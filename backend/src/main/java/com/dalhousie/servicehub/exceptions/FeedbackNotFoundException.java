package com.dalhousie.servicehub.exceptions;

public class FeedbackNotFoundException extends RuntimeException {
    public FeedbackNotFoundException(String message) {
        super(message);
    }
}
