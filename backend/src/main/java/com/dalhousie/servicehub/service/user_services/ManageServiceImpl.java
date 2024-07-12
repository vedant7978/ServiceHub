package com.dalhousie.servicehub.service.user_services;

import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.mapper.ServiceMapper;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.AddServiceRequest;
import com.dalhousie.servicehub.request.UpdateServiceRequest;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@Service
@RequiredArgsConstructor
public class ManageServiceImpl implements ManageService {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceMapper serviceMapper;

    @Override
    public ResponseBody<Object> addService(AddServiceRequest addServiceRequest, Long providerId) {
        if (!userRepository.existsById(providerId)) {
            throw new UserNotFoundException("User not found for id: " + providerId);
        }

        ServiceModel serviceModel = ServiceModel.builder()
                .description(addServiceRequest.getDescription())
                .name(addServiceRequest.getName())
                .perHourRate(addServiceRequest.getPerHourRate())
                .type(addServiceRequest.getType())
                .providerId(providerId)
                .build();

        serviceRepository.save(serviceModel);

        return new ResponseBody<>(SUCCESS, "", "Add service successful");
    }

    @Override
    public ResponseBody<GetServicesResponse> getUserServicesByProviderId(Long providerId) {
        UserModel user = userRepository.findById(providerId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + providerId));

        List<ServiceDto> services = serviceRepository.findByProviderId(user.getId())
                .stream()
                .map(serviceMapper::toDto)
                .collect(Collectors.toList());

        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();

        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully");
    }

    @Override
    public ResponseBody<Object> deleteService(Long serviceId) {

        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with ID: " + serviceId));

        serviceRepository.delete(service);
        return new ResponseBody<>(SUCCESS, null, "Service deleted successfully");
    }

    @Override
    public ResponseBody<Object> updateService(UpdateServiceRequest updateServiceRequest, Long providerId) {
        if (!serviceRepository.existsById(updateServiceRequest.getId())) {
            throw new ServiceNotFoundException("Service not found for id: " + updateServiceRequest.getId());
        }

        serviceRepository.updateService(
                updateServiceRequest.getId(),
                updateServiceRequest.getDescription(),
                updateServiceRequest.getName(),
                updateServiceRequest.getPerHourRate(),
                updateServiceRequest.getType(),
                providerId
        );

        return new ResponseBody<>(SUCCESS, "", "Update service successful");
    }
}
