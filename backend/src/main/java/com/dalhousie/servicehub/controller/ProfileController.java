package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.UpdateUserRequest;
import com.dalhousie.servicehub.service.profile.ProfileService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profile")
public class ProfileController {

    private static final Logger logger = LogManager.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;

    @GetMapping("/get-user-details")
    public ResponseEntity<Object> getUserDetails(@RequestParam String email) {
        try {
            UserModel userModel = profileService.getUserByEmail(email);
            logger.info("Fetched user details for email: {}", email);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error fetching user details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-user-details")
    public ResponseEntity<Object> updateUserDetails(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            UserModel updatedUser = profileService.updateUser(updateUserRequest);
            logger.info("Updated user details for email: {}", updateUserRequest.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (UsernameNotFoundException e) {
            logger.error("User update failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during user update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@AuthenticationPrincipal UserModel userModel, @RequestParam String newPassword) {
        try {
            profileService.resetPassword(userModel.getId(), newPassword);
            logger.info("Password reset successfully for user: {}", userModel.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully");
        } catch (Exception e) {
            logger.error("Error resetting password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}