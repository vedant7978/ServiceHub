package com.dalhousie.servicehub.service.wishlist;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.response.GetWishlistResponse;
import com.dalhousie.servicehub.util.ResponseBody;

import java.util.List;

public interface WishlistService {

    /**
     * Add a service to wishlist for requesting UserModel
     * @param serviceId ID of the service
     * @param userModel UserModel instance
     * @return ResponseBody object for String representing api result
     */
    ResponseBody<String> addWishlist(Long serviceId, UserModel userModel);

    /**
     * Provides list of all services added to wishlist by requesting user id
     * @param userId ID of the user
     * @return ResponseBody object for List of GetWishlistResponse
     */
    ResponseBody<List<GetWishlistResponse>> getWishlists(Long userId);
}
