package com.dalhousie.servicehub.exceptions;

import org.hibernate.service.spi.ServiceException;

public class ServiceNotFoundException extends ServiceException {
    public ServiceNotFoundException(String message) {
        super(message);
    }
}