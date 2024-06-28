package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.response.GetWishlistResponse;
import com.dalhousie.servicehub.service.wishlist.WishlistService;
import com.dalhousie.servicehub.util.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private static final Logger logger = LogManager.getLogger(WishlistController.class);

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add-wishlist")
    public ResponseEntity<ResponseBody<String>> addWishlist(
            @RequestParam Long serviceId,
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Add wishlist request received for service ID: {}", serviceId);
            ResponseBody<String> responseBody = wishlistService.addWishlist(serviceId, userModel);
            logger.info("Add wishlist request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to add wishlist, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getFailureResponseBody(exception.getMessage()));
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while adding wishlist, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getFailureResponseBody(exception.getMessage()));
        }
    }

    @GetMapping("/get-wishlist")
    public ResponseEntity<ResponseBody<GetWishlistResponse>> getWishlists(
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Get wishlists request received for user {}", userModel.getId());
            ResponseBody<GetWishlistResponse> responseBody = wishlistService.getWishlists(userModel.getId());
            logger.info("Get wishlists request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("User not found, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBody<>(FAILURE, null, exception.getMessage()));
        } catch (Exception exception) {
            logger.error("Error occurred while getting wishlists, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBody<>(FAILURE, null, exception.getMessage()));
        }
    }

    private ResponseBody<String> getFailureResponseBody(String message) {
        return new ResponseBody<>(FAILURE, null, message);
    }
}
