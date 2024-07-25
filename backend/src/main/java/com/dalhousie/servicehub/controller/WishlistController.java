package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.factory.service.ServiceFactory;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.AddWishlistRequest;
import com.dalhousie.servicehub.response.GetWishlistResponse;
import com.dalhousie.servicehub.service.wishlist.WishlistService;
import com.dalhousie.servicehub.util.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private static final Logger logger = LogManager.getLogger(WishlistController.class);
    private final WishlistService wishlistService;

    public WishlistController(ServiceFactory serviceFactory) {
        wishlistService = serviceFactory.getWishlistService();
    }

    @PostMapping("/add-wishlist")
    public ResponseEntity<ResponseBody<String>> addWishlist(
            @RequestBody AddWishlistRequest request,
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Add wishlist request received for service ID: {}", request.getServiceId());
            ResponseBody<String> responseBody = wishlistService.addWishlist(request.getServiceId(), userModel);
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
            ResponseBody<GetWishlistResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while getting contract feedback, {}", exception.getMessage());
            ResponseBody<GetWishlistResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @DeleteMapping("/delete-wishlist")
    public ResponseEntity<ResponseBody<String>> deleteWishlist(@RequestParam Long wishlistId) {
        try {
            logger.info("Delete wishlist request received for wishlistId: {}", wishlistId);
            ResponseBody<String> responseBody = wishlistService.deleteWishlist(wishlistId);
            logger.info("Delete wishlist request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while deleting wishlist, {}", exception.getMessage());
            return ResponseEntity.badRequest().body(getFailureResponseBody(exception.getMessage()));
        }
    }

    private ResponseBody<String> getFailureResponseBody(String message) {
        return new ResponseBody<>(FAILURE, null, message);
    }
}
