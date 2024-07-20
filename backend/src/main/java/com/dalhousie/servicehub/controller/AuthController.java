package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.BlackListTokenAlreadyExistsException;
import com.dalhousie.servicehub.exceptions.InvalidTokenException;
import com.dalhousie.servicehub.exceptions.UserAlreadyExistException;
import com.dalhousie.servicehub.request.AuthenticationRequest;
import com.dalhousie.servicehub.request.ForgotPasswordRequest;
import com.dalhousie.servicehub.request.RegisterRequest;
import com.dalhousie.servicehub.request.ResetPasswordRequest;
import com.dalhousie.servicehub.response.AuthenticationResponse;
import com.dalhousie.servicehub.service.user.UserService;
import com.dalhousie.servicehub.util.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseBody<AuthenticationResponse>> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        try {
            ResponseBody<AuthenticationResponse> body = userService.registerUser(registerRequest);
            logger.info("User registered successfully with email: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (UserAlreadyExistException exception) {
            logger.error("User registration failed: {}", exception.getMessage());
            ResponseBody<AuthenticationResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error during user registration: {}", exception.getMessage());
            ResponseBody<AuthenticationResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBody<AuthenticationResponse>> loginUser(
            @Valid @RequestBody AuthenticationRequest authenticationRequest
    ) {
        try {
            ResponseBody<AuthenticationResponse> body = userService.authenticateUser(authenticationRequest);
            logger.info("User login successful for email: {}", authenticationRequest.getEmail());
            return ResponseEntity.ok(body);
        } catch (UsernameNotFoundException exception) {
            logger.error("User login failed: {}", exception.getMessage());
            ResponseBody<AuthenticationResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error during user login: {}", exception.getMessage());
            ResponseBody<AuthenticationResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseBody<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {
        try {
            ResponseBody<String> body = userService.resetPassword(resetPasswordRequest);
            logger.info("Successfully resetted password for {}", resetPasswordRequest.getEmail());
            return ResponseEntity.ok(body);
        } catch (UsernameNotFoundException exception) {
            logger.error("Field to reset password due to user not found: {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (InvalidTokenException exception) {
            logger.error("Field to reset password due to invalid token: {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error during resetting password for {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseBody<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request,
            HttpServletRequest servletRequest
    ) {
        try {
            ResponseBody<String> body = userService.forgotPassword(request.getEmail(), servletRequest);
            logger.info("Mail sent successfully for resetting password");
            return ResponseEntity.ok(body);
        } catch (Exception exception) {
            logger.error("Failed to sent mail for resetting password: {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.internalServerError().body(body);
        }
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ResponseBody<String>> signOut(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            ResponseBody<String> body = userService.signOut(token);
            logger.info("User signed out successfully.");
            return ResponseEntity.ok(body);
        } catch (BlackListTokenAlreadyExistsException exception) {
            logger.error("user is already logged out: {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error during user sign-out: {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }
}
