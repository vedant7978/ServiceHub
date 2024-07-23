package com.dalhousie.servicehub.mapper;

import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.model.ServiceModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceMapper implements Mapper<ServiceModel, ServiceDto> {

    private final ModelMapper modelMapper;

    @Override
    public ServiceDto toDto(ServiceModel serviceModel) {
        return modelMapper.map(serviceModel, ServiceDto.class);
    }

    @Override
    public ServiceModel toEntity(ServiceDto serviceDto) {
        return modelMapper.map(serviceDto, ServiceModel.class);
    }
}
