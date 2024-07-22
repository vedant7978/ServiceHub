package com.dalhousie.servicehub.service.wishlist;

import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.exceptions.WishlistNotFoundException;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.model.WishlistModel;
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
    public ResponseBody<List<GetWishlistResponse>> getWishlists(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        List<GetWishlistResponse> wishlists = wishlistRepository.findAllByUser(user)
                .stream()
                .map(this::getWishlistResponse)
                .toList();
        return new ResponseBody<>(SUCCESS, wishlists, "Get wishlists successful");
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
     * @return GetWishlistResponse instance
     */
    private GetWishlistResponse getWishlistResponse(WishlistModel wishlistModel) {
        ServiceModel serviceModel = wishlistModel.getService();
        UserModel serviceProvider = userRepository.findById(serviceModel.getProviderId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + serviceModel.getProviderId()));
        Double serviceProviderRating = feedbackService.getAverageRatingForUser(serviceModel.getProviderId());

        return GetWishlistResponse.builder()
                .id(wishlistModel.getId())
                .serviceId(serviceModel.getId())
                .providerId(serviceModel.getProviderId())
                .serviceProviderImage(serviceProvider.getImage())
                .name(serviceModel.getName())
                .type(serviceModel.getType())
                .description(serviceModel.getDescription())
                .serviceProviderRating(serviceProviderRating)
                .perHourRate(serviceModel.getPerHourRate())
                .build();
    }
}
