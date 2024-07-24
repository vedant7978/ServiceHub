package com.dalhousie.servicehub.service.dashboard_services;

import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.enums.ContractStatus;
import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.mapper.ServiceMapper;
import com.dalhousie.servicehub.model.ContractModel;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ContractRepository;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.repository.WishlistRepository;
import com.dalhousie.servicehub.request.ContractRequest;
import com.dalhousie.servicehub.response.GetProviderResponse;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import com.dalhousie.servicehub.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@Service
@RequiredArgsConstructor
public class DashboardServicesImpl implements DashboardServices {

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final ContractRepository contractRepository;

    private final ServiceMapper serviceMapper;
    private final FeedbackService feedbackService;

    @Override
    public ResponseBody<GetServicesResponse> getAllServices() {
        Long loggedInUserId = SecurityUtils.getLoggedInUserId();
        List<ServiceDto> services = getProcessedServiceDtoList(serviceRepository.findAll(), loggedInUserId);
        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully");
    }

    @Override
    public ResponseBody<GetServicesResponse> getServicesByType(ServiceType type) {
        Long loggedInUserId = SecurityUtils.getLoggedInUserId();
        List<ServiceDto> services = getProcessedServiceDtoList(serviceRepository.findByType(type), loggedInUserId);
        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully based on the type of the service");
    }

    @Override
    public ResponseBody<GetServicesResponse> searchServicesByName(String name) {
        Long loggedInUserId = SecurityUtils.getLoggedInUserId();
        List<ServiceDto> services = getProcessedServiceDtoList(
                serviceRepository.findByNameContainingIgnoreCase(name),
                loggedInUserId
        );
        GetServicesResponse response = GetServicesResponse.builder()
                .services(services)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Fetched user services successfully based on the name of the service");
    }

    @Override
    public ResponseBody<GetProviderResponse> getProviderDetailsById(Long providerId) {
        UserModel user = userRepository.findById(providerId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + providerId));
        GetProviderResponse response = GetProviderResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .image(user.getImage())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        return new ResponseBody<>(SUCCESS, response, "Fetched provider details successfully");
    }

    @Override
    public ResponseBody<String> requestService(ContractRequest contractRequest,Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));
        ServiceModel service = serviceRepository.findById(contractRequest.getServiceId())
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with ID: " + contractRequest.getServiceId()));

        ContractModel contract = ContractModel.builder()
                .address(contractRequest.getAddress())
                .service(service)
                .user(user)
                .status(ContractStatus.Pending)
                .build();
        contractRepository.save(contract);
        return new ResponseBody<>(SUCCESS, "Service requested successfully", null);
    }

    /**
     * Provides processed list of serviceDto from list of serviceModel
     * @param serviceModelList List of ServiceModel to process
     * @param loggedInUserId ID of the logged-in user
     * @return List of ServiceDto after processing list of service model
     * Note: Processing includes:
     * <li>Filtering all services from current logged-in user</li>
     * <li>Mapping ServiceModel to ServiceDto and set derived variables</li>
     */
    private List<ServiceDto> getProcessedServiceDtoList(List<ServiceModel> serviceModelList, Long loggedInUserId) {
        return serviceModelList.stream()
                .filter(service -> !service.getProviderId().equals(loggedInUserId))
                .map(serviceMapper::toDto)
                .peek(serviceDto -> {
                    serviceDto.setAddedToWishlist(isAddedToWishlist(serviceDto.getId(), loggedInUserId));
                    serviceDto.setAverageRating(feedbackService.getAverageRatingForUser(serviceDto.getProviderId()));
                    serviceDto.setFeedbacks(feedbackService.getFeedbacks(serviceDto.getProviderId()).data().getFeedbacks());
                    serviceDto.setRequested(contractRepository.existsByServiceIdAndUserId(serviceDto.getId(), loggedInUserId));
                    userRepository.findById(serviceDto.getProviderId()).ifPresent(
                            (provider) -> serviceDto.setProviderImage(provider.getImage())
                    );
                })
                .toList();
    }

    /**
     * Checks if requesting userId has provided serviceId added as wishlist or not
     * @param serviceId ID of the service
     * @param userId ID of the user
     * @return True if serviceId is added to wishlist, False otherwise
     */
    private boolean isAddedToWishlist(Long serviceId, Long userId) {
        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with ID: " + serviceId));
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));
        return wishlistRepository.existsByServiceAndUser(service, user);
    }
}
