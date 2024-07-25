package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.PasswordNotMatchingException;
import com.dalhousie.servicehub.factory.service.ServiceFactory;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.NewPasswordRequest;
import com.dalhousie.servicehub.request.UpdateUserRequest;
import com.dalhousie.servicehub.response.UserDetailsResponse;
import com.dalhousie.servicehub.service.profile.ProfileService;
import com.dalhousie.servicehub.util.ResponseBody;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/user-profile")
public class ProfileController {

    private static final Logger logger = LogManager.getLogger(ProfileController.class);
    private final ProfileService profileService;

    public ProfileController(ServiceFactory serviceFactory) {
        profileService = serviceFactory.getProfileService();
    }

    @GetMapping("/get-user-details")
    public ResponseEntity<ResponseBody<UserDetailsResponse>> getUserDetails(
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            ResponseBody<UserDetailsResponse> body = profileService.getUserDetailsResponse(userModel);
            logger.info("Fetched user details for id: {}", userModel.getId());
            return ResponseEntity.ok(body);
        } catch (Exception exception) {
            logger.error("Unexpected error fetching user details: {}", exception.getMessage());
            ResponseBody<UserDetailsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PutMapping("/update-user-details")
    public ResponseEntity<ResponseBody<UserDetailsResponse>> updateUserDetails(
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        try {
            ResponseBody<UserDetailsResponse> body = profileService.updateUser(updateUserRequest);
            logger.info("Updated user details for email: {}", updateUserRequest.getEmail());
            return ResponseEntity.ok(body);
        } catch (UsernameNotFoundException exception) {
            logger.error("User update failed: {}", exception.getMessage());
            ResponseBody<UserDetailsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error during user update: {}", exception.getMessage());
            ResponseBody<UserDetailsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PutMapping("/new-password")
    public ResponseEntity<ResponseBody<String>> newPassword(
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody NewPasswordRequest newPasswordRequest
    ) {
        try {
            ResponseBody<String> body = profileService.newPassword(
                    userModel.getId(),
                    newPasswordRequest.getOldPassword(),
                    newPasswordRequest.getNewPassword()
            );
            logger.info("Password reset successfully for user: {}", userModel.getEmail());
            return ResponseEntity.ok(body);
        } catch (PasswordNotMatchingException exception) {
            logger.error("Error in resetting password: {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Error resetting password: {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }
}