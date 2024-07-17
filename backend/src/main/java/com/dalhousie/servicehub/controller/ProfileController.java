package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.PasswordNotMatchingException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.NewPasswordRequest;
import com.dalhousie.servicehub.request.UpdateUserRequest;
import com.dalhousie.servicehub.response.UserDetailsResponse;
import com.dalhousie.servicehub.service.profile.ProfileService;
import com.dalhousie.servicehub.util.ResponseBody;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;
import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@RestController
@RequestMapping("/api/user-profile")
public class ProfileController {

    private static final Logger logger = LogManager.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;

    @GetMapping("/get-user-details")
    public ResponseEntity<ResponseBody<UserDetailsResponse>> getUserDetails(@AuthenticationPrincipal UserModel userModel) {
        try {
            logger.info("Fetched user details for id: {}", userModel.getId());
            UserDetailsResponse userDetailsResponse = profileService.getUserDetailsResponse(userModel);
            ResponseBody<UserDetailsResponse> body = new ResponseBody<>(SUCCESS, userDetailsResponse, "Fetched user details");
            return ResponseEntity.status(HttpStatus.OK).body(body);
        } catch (Exception e) {
            logger.error("Unexpected error fetching user details: {}", e.getMessage());
            ResponseBody<UserDetailsResponse> body = new ResponseBody<>(FAILURE, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @PutMapping("/update-user-details")
    public ResponseEntity<ResponseBody<UserDetailsResponse>> updateUserDetails(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            UserDetailsResponse updatedUser = profileService.updateUser(updateUserRequest);
            logger.info("Updated user details for email: {}", updateUserRequest.getEmail());
            ResponseBody<UserDetailsResponse> body = new ResponseBody<>(SUCCESS, updatedUser, "User updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(body);
        } catch (UsernameNotFoundException e) {
            logger.error("User update failed: {}", e.getMessage());
            ResponseBody<UserDetailsResponse> body = new ResponseBody<>(FAILURE, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        } catch (Exception e) {
            logger.error("Unexpected error during user update: {}", e.getMessage());
            ResponseBody<UserDetailsResponse> body = new ResponseBody<>(FAILURE, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @PutMapping("/new-password")
    public ResponseEntity<ResponseBody<String>> newPassword(
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody NewPasswordRequest newPasswordRequest
    ) {
        try {
            profileService.newPassword(userModel.getId(), newPasswordRequest.getOldPassword(), newPasswordRequest.getNewPassword());
            logger.info("Password reset successfully for user: {}", userModel.getEmail());
            ResponseBody<String> body = new ResponseBody<>(SUCCESS, "" , "Password reset successfully");
            return ResponseEntity.status(HttpStatus.OK).body(body);
        } catch (PasswordNotMatchingException exception) {
            logger.error("Error in resetting password: {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception e) {
            logger.error("Error resetting password: {}", e.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }
}