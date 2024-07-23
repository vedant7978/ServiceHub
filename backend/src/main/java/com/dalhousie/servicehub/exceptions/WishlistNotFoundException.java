package com.dalhousie.servicehub.exceptions;

public class WishlistNotFoundException extends RuntimeException{
    public WishlistNotFoundException(String message) {
        super(message);
    }
}
