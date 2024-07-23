package com.dalhousie.servicehub.service.wishlist;

import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.exceptions.WishlistNotFoundException;
import com.dalhousie.servicehub.mapper.ServiceMapper;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.model.WishlistModel;
import com.dalhousie.servicehub.repository.ContractRepository;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.repository.WishlistRepository;
import com.dalhousie.servicehub.response.GetWishlistResponse;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final FeedbackService feedbackService;
    private final ServiceMapper serviceMapper;
    private final ContractRepository contractRepository;

    @Override
    public ResponseBody<String> addWishlist(Long serviceId, UserModel userModel) {
        ServiceModel service_ID = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with ID: " + serviceId));

        WishlistModel wishlistModel = WishlistModel.builder()
                .service(service_ID)
                .user(userModel)
                .build();
        wishlistRepository.save(wishlistModel);
        return new ResponseBody<>(SUCCESS, "Wishlist added successfully", "Wishlist added successfully");
    }

    @Override
    public ResponseBody<GetWishlistResponse> getWishlists(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        List<ServiceDto> services = wishlistRepository.findAllByUser(user)
                .stream()
                .map(wishlistModel -> getServiceSto(wishlistModel, userId))
                .toList();
        GetWishlistResponse response = GetWishlistResponse.builder().services(services).build();
        return new ResponseBody<>(SUCCESS, response, "Get wishlists successful");
    }

    @Override
    public ResponseBody<String> deleteWishlist(Long wishlistId) {
        WishlistModel wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException("Wishlist not found with ID: " + wishlistId));

        wishlistRepository.delete(wishlist);
        return new ResponseBody<>(SUCCESS, "", "Wishlist deleted successfully");
    }

    /**
     * Convert WishlistModel to GetWishlistResponse
     * @param wishlistModel WishlistModel to convert
     * @param loggedInUserId ID of the logged-in user
     * @return GetWishlistResponse instance
     */
    private ServiceDto getServiceSto(WishlistModel wishlistModel, Long loggedInUserId) {
        ServiceModel serviceModel = wishlistModel.getService();
        UserModel serviceProvider = userRepository.findById(serviceModel.getProviderId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + serviceModel.getProviderId()));

        ServiceDto serviceDto = serviceMapper.toDto(serviceModel);
        serviceDto.setId(wishlistModel.getId());
        serviceDto.setAddedToWishlist(true);
        serviceDto.setAverageRating(feedbackService.getAverageRatingForUser(serviceDto.getProviderId()));
        serviceDto.setFeedbacks(feedbackService.getFeedbacks(serviceDto.getProviderId()).data().getFeedbacks());
        serviceDto.setProviderImage(serviceProvider.getImage());
        serviceDto.setRequested(contractRepository.existsByServiceIdAndUserId(serviceDto.getId(), loggedInUserId));
        return serviceDto;
    }
}
