package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.InvalidTokenException;
import com.dalhousie.servicehub.exceptions.UserAlreadyExistException;
import com.dalhousie.servicehub.request.AuthenticationRequest;
import com.dalhousie.servicehub.request.RegisterRequest;
import com.dalhousie.servicehub.request.ResetPasswordRequest;
import com.dalhousie.servicehub.response.AuthenticationResponse;
import com.dalhousie.servicehub.service.user.UserService;
import com.dalhousie.servicehub.util.Constants;
import com.dalhousie.servicehub.util.forgotPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> userRegisterHandler(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthenticationResponse authenticationResponse = userService.registerUser(registerRequest);
            logger.info("User registered successfully with email: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
        } catch (UserAlreadyExistException e) {
            logger.error("User registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> userLoginHandler(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            AuthenticationResponse authenticationResponse = userService.authenticateUser(authenticationRequest);
            logger.info("User login successful for email: {}", authenticationRequest.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
        } catch (UsernameNotFoundException e) {
            logger.error("User login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during user login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            userService.resetPassword(
                    resetPasswordRequest.getEmail(),
                    resetPasswordRequest.getPassword(),
                    resetPasswordRequest.getToken()
            );
            logger.info("Successfully resetted password for {}", resetPasswordRequest.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(Constants.RESET_PASSWORD_SUCCESS_MESSAGE);
        } catch (UsernameNotFoundException e) {
            logger.error("Field to reset password due to user not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InvalidTokenException e) {
            logger.error("Field to reset password due to invalid token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during resetting password for {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> processForgotPassword(@Valid @RequestBody forgotPasswordRequest request, HttpServletRequest servletRequest) {
        try {
            String resetUrl = userService.getURL(servletRequest) + "/reset-password";
            userService.forgotPassword(request.getEmail(), resetUrl);
            return ResponseEntity.ok("Reset link is sent to your email");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
