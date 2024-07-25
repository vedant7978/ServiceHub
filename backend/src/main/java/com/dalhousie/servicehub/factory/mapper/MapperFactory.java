package com.dalhousie.servicehub.factory.mapper;

import com.dalhousie.servicehub.mapper.ServiceMapper;

public interface MapperFactory {
    ServiceMapper getServiceMapper();
}
