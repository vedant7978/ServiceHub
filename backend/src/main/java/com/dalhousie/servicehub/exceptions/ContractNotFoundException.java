package com.dalhousie.servicehub.exceptions;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(String message) {
        super(message);
    }
}
