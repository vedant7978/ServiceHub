package com.dalhousie.servicehub.service.wishlist;

import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private final WishlistRepository wishlistRepository;

    @Autowired
    private final ServiceRepository serviceRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
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
                .map(wishlist -> {
                    ServiceModel serviceModel = wishlist.getService();
                    UserModel serviceProvider = userRepository.findById(serviceModel.getProviderId())
                            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + serviceModel.getProviderId()));
                    Double serviceProviderRating = feedbackService.getAverageRatingForUser(serviceModel.getProviderId());

                    return GetWishlistResponse.builder()
                            .id(serviceModel.getId())
                            .providerId(serviceModel.getProviderId())
                            .serviceProviderImage(serviceProvider.getImage())
                            .name(serviceModel.getName())
                            .type(serviceModel.getType())
                            .description(serviceModel.getDescription())
                            .serviceProviderRating(serviceProviderRating)
                            .perHourRate(serviceModel.getPerHourRate())
                            .build();
                })
                .collect(Collectors.toList());

        return new ResponseBody<>(SUCCESS, wishlists, "Get wishlists successful");
    }
}