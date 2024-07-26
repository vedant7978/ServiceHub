package com.dalhousie.servicehub.factory.mapper;

import com.dalhousie.servicehub.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation class for MapperFactory.
 * This factory also follows singleton design pattern. Since Mappers are autowired,
 * springboot by default provides singleton instance over here.
 */
@Component
@RequiredArgsConstructor
public class MapperFactoryImpl implements MapperFactory {

    private final ServiceMapper serviceMapper;

    @Override
    public ServiceMapper getServiceMapper() {
        return serviceMapper;
    }
}
