package com.dalhousie.servicehub.factory.mapper;

import com.dalhousie.servicehub.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperFactoryImpl implements MapperFactory {

    private final ServiceMapper serviceMapper;

    @Override
    public ServiceMapper getServiceMapper() {
        return serviceMapper;
    }
}
