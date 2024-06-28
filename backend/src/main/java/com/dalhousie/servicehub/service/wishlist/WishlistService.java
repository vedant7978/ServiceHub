package com.dalhousie.servicehub.service.wishlist;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.response.GetWishlistResponse;
import com.dalhousie.servicehub.util.ResponseBody;

public interface WishlistService {
    ResponseBody<String> addWishlist(Long serviceId, UserModel userModel);
    ResponseBody<GetWishlistResponse> getWishlists(Long userId);
}
