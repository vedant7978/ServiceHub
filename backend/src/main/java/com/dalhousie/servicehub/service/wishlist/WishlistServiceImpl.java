package com.dalhousie.servicehub.service.wishlist;

import com.dalhousie.servicehub.dto.WishlistDto;
import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.mapper.WishlistMapper;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.model.WishlistModel;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.repository.WishlistRepository;
import com.dalhousie.servicehub.response.GetWishlistResponse;
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

    private final WishlistMapper wishlistMapper;

    @Override
    public ResponseBody<String> addWishlist(Long serviceId, UserModel userModel) {
        if (!serviceRepository.existsById(serviceId)) {
            throw new ServiceNotFoundException("Service not found with ID: " + serviceId);
        }
        WishlistModel wishlistModel = WishlistModel.builder()
                .serviceId(serviceId)
                .userId(userModel.getId())
                .build();

        wishlistRepository.save(wishlistModel);
        return new ResponseBody<>(SUCCESS, "Wishlist added successfully", "Wishlist added successfully");
    }

    @Override
    public ResponseBody<GetWishlistResponse> getWishlists(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException("User not found for id: " + userId);

        List<WishlistDto> wishlists = wishlistRepository.findAllByUserId(userId)
                .stream()
                .map(wishlistMapper::toDto)
                .collect(Collectors.toList());

        GetWishlistResponse response = GetWishlistResponse.builder()
                .wishlists(wishlists)
                .build();

        return new ResponseBody<>(SUCCESS, response, "Get wishlists successful");
    }
}
