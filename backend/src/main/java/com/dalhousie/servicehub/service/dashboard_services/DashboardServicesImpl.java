package com.dalhousie.servicehub.service.dashboard_services;


import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.dto.UserDto;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.mapper.ServiceMapper;
import com.dalhousie.servicehub.mapper.UserMapper;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.AddServiceRequest;
import com.dalhousie.servicehub.request.UpdateServiceRequest;
import com.dalhousie.servicehub.response.GetProviderResponse;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.service.dashboard_services.DashboardServices;
import com.dalhousie.servicehub.util.ResponseBody;
import com.dalhousie.servicehub.enums.ServiceType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;
import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;


@Service
@RequiredArgsConstructor
public class DashboardServicesImpl implements DashboardServices {
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    private final ServiceMapper serviceMapper;
    private final UserMapper userMapper;


    @Override
    public ResponseBody<GetServicesResponse> getAllServices() {
        List<ServiceDto> services = serviceRepository.findAll()
                .stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());

        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();

        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully");

    }

    @Override
    public ResponseBody<GetServicesResponse> getServicesByType(ServiceType type) {
        List<ServiceDto> services = serviceRepository.findByType(type)
                .stream()
                .map(serviceMapper::toDto)
                .toList();

        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully based on the type of the service");
    }
    @Override
    public ResponseBody<GetServicesResponse> searchServicesByName(String name) {
        List<ServiceDto> services = serviceRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(serviceMapper::toDto)
                .toList();

        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully based on the name of the service");

    }
    @Override
    public ResponseBody<GetProviderResponse> getProviderDetailsById(Long providerId) {
        Optional<UserModel> userModelOptional = userRepository.findById(providerId);

        if (userModelOptional.isPresent()) {
            UserDto userDto = userMapper.toDto(userModelOptional.get());

            GetProviderResponse response = GetProviderResponse.builder()
                    .provider(userDto)
                    .build();

            return new ResponseBody<>(SUCCESS, response, "Fetched provider details successfully");
        } else {
            return new ResponseBody<>(FAILURE, null, "Provider not found");
        }
    }
}
