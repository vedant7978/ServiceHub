package com.dalhousie.servicehub.factory.mapper;

import com.dalhousie.servicehub.mapper.ServiceMapper;

/**
 * Factory pattern for mappers used in the application
 * This factory is responsible for creating objects for all mappers used in the application.
 */
public interface MapperFactory {

    /**
     * Provides the service mapper
     * @return ServiceMapper instance
     */
    ServiceMapper getServiceMapper();
}
