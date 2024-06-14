package com.dalhousie.servicehub.mapper;

import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.model.ServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper implements Mapper<ServiceModel, ServiceDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ServiceDto toDto(ServiceModel serviceModel) {
        return modelMapper.map(serviceModel, ServiceDto.class);
    }

    @Override
    public ServiceModel toEntity(ServiceDto serviceDto) {
        return modelMapper.map(serviceDto, ServiceModel.class);
    }
}
