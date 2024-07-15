package com.dalhousie.servicehub.service.dashboard_services;


import com.dalhousie.servicehub.controller.DashboardController;
import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.dto.UserDto;
import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.mapper.ServiceMapper;
import com.dalhousie.servicehub.mapper.UserMapper;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.response.GetProviderResponse;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.util.ResponseBody;
import com.dalhousie.servicehub.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(DashboardController.class);

    @Override
    public ResponseBody<GetServicesResponse> getAllServices() {
        Long loggedInUserId = SecurityUtils.getLoggedInUserId();

        List<ServiceDto> services = serviceRepository.findAll()
                .stream()
                .filter(service -> !service.getProviderId().equals(loggedInUserId))
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());

        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();

        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully");

    }

    @Override
    public ResponseBody<GetServicesResponse> getServicesByType(ServiceType type) {
        Long loggedInUserId = SecurityUtils.getLoggedInUserId();
        List<ServiceDto> services = serviceRepository.findByType(type)
                .stream()
                .filter(service -> !service.getProviderId().equals(loggedInUserId))
                .map(serviceMapper::toDto)
                .toList();

        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully based on the type of the service");
    }
    @Override
    public ResponseBody<GetServicesResponse> searchServicesByName(String name) {
        Long loggedInUserId = SecurityUtils.getLoggedInUserId();
        List<ServiceDto> services = serviceRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .filter(service -> !service.getProviderId().equals(loggedInUserId))
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
