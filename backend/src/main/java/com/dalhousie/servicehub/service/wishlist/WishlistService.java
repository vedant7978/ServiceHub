package com.dalhousie.servicehub.service.wishlist;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.response.GetWishlistResponse;
import com.dalhousie.servicehub.util.ResponseBody;

import java.util.List;

public interface WishlistService {
    ResponseBody<String> addWishlist(Long serviceId, UserModel userModel);
    ResponseBody<List<GetWishlistResponse>> getWishlists(Long userId);
}
