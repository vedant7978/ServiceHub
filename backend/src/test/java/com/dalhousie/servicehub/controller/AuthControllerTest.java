package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.service.UserServiceImpl;
import com.dalhousie.servicehub.util.AuthenticationRequest;
import com.dalhousie.servicehub.util.AuthenticationResponse;
import com.dalhousie.servicehub.service.JwtService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final Logger logger = LogManager.getLogger(AuthControllerTest.class);
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserServiceImpl userService;
    private UserModel userModel;
    private AuthenticationRequest authRequest;

    @BeforeEach
    void setUp() {
        userModel = UserModel.builder()
                .name("Jems Patel")
                .email("jems007patel@gmail.com")
                .password("Jems@007")
                .phone("7825258252")
                .address("1881 Brunswick Street")
                .image("image.jpg")
                .build();

        authRequest = new AuthenticationRequest("jems007patel@gmail.com", "Jems@007");
    }

    @Test
    @DisplayName("Registration Successfully Completed")
    void UserRegisterHandlerTest() {
        logger.info("Starting test: Registration Successfully Completed");
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-pass");
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        logger.info("Registering user with details: {}", userModel);
        AuthenticationResponse response = userService.registerUser(userModel);

        logger.info("Received response: {}", response);
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());

        verify(userRepository, times(1)).save(any(UserModel.class));
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));

        logger.info("Test completed: Registration Successfully Completed");
    }

    @Test
    @DisplayName("Successfully login")
    void userLoginHandlerTest() {
        logger.info("Starting test: Successfully login");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userModel));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        logger.info("Authenticating user with request: {}", authRequest);
        AuthenticationResponse response = userService.authenticateUser(authRequest);

        logger.info("Received response: {}", response);
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
        logger.info("Test completed: Successfully login");

    }

    @Test
    @DisplayName("Login with Invalid Credentials")
    void userLoginHandlerTest_WithInvalidCredentials() {
        logger.info("Starting test: Login with Invalid Credentials");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        AuthenticationRequest invalidAuthRequest = new AuthenticationRequest("jems007patel@gmail.com", "wrongpassword");

        logger.info("Authenticating user with invalid request: {}", invalidAuthRequest);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticateUser(invalidAuthRequest);
        });

        logger.info("Exception thrown: {}", exception.getMessage());
        assertEquals("Bad credentials", exception.getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(0)).findByEmail(anyString());
        verify(jwtService, times(0)).generateToken(any(UserDetails.class));

        logger.info("Test completed: Login with Invalid Credentials");
    }

    @Test
    @DisplayName("Login with Non-existing User")
    void userLoginHandlerTest_WithNonExistingUser() {
        logger.info("Starting test: Login with Non-existing User");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        logger.info("Authenticating user with request: {}", authRequest);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.authenticateUser(authRequest);
        });

        logger.info("Exception thrown: {}", exception.getMessage());assertEquals("User not found with email: jems007patel@gmail.com", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(jwtService, times(0)).generateToken(any(UserDetails.class));

        logger.info("Test completed: Login with Non-existing User");
    }
}
