package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.exceptions.UserAlreadyExistException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.AuthenticationRequest;
import com.dalhousie.servicehub.request.RegisterRequest;
import com.dalhousie.servicehub.response.AuthenticationResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Logger logger = LogManager.getLogger(UserServiceTest.class);

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    @Autowired
    private UserServiceImpl userService;

    private UserModel userModel;
    private AuthenticationRequest authRequest;
    private RegisterRequest registerRequest;

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
        registerRequest = RegisterRequest.builder()
                .name("Jems Patel")
                .email("jems007patel@gmail.com")
                .password("Jems@007")
                .phone("7825258252")
                .address("1881 Brunswick Street")
                .image("image.jpg")
                .build();
    }

    @Test
    void shouldNotRegister_WhenUserIsAlreadyRegistered() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userModel));

        // When
        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () ->
                userService.registerUser(registerRequest)
        );

        // Then
        assertEquals(exception.getMessage(), "User with this email already exists.");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Registration Successfully Completed")
    void UserRegisterHandlerTest() {
        logger.info("Starting test: Registration Successfully Completed");
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        logger.info("Registering user with details: {}", userModel);
        AuthenticationResponse response = userService.registerUser(registerRequest);

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
        verify(userRepository, times(2)).findByEmail(anyString());
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
        logger.info("Test completed: Successfully login");

    }

    @Test
    @DisplayName("Login with Invalid Credentials")
    void userLoginHandlerTest_WithInvalidCredentials() {
        logger.info("Starting test: Login with Invalid Credentials");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));
        when(userRepository.findByEmail("jems007patel@gmail.com")).thenReturn(Optional.of(userModel));

        AuthenticationRequest invalidAuthRequest = new AuthenticationRequest("jems007patel@gmail.com", "wrongpassword");

        logger.info("Authenticating user with invalid request: {}", invalidAuthRequest);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticateUser(invalidAuthRequest);
        });

        logger.info("Exception thrown: {}", exception.getMessage());
        assertEquals("Bad credentials", exception.getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(0)).generateToken(any(UserDetails.class));

        logger.info("Test completed: Login with Invalid Credentials");
    }

    @Test
    @DisplayName("Login with Non-existing User")
    void userLoginHandlerTest_WithNonExistingUser() {
        logger.info("Starting test: Login with Non-existing User");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        logger.info("Authenticating user with request: {}", authRequest);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.authenticateUser(authRequest);
        });

        logger.info("Exception thrown: {}", exception.getMessage());
        assertEquals("User not found with email: jems007patel@gmail.com", exception.getMessage());
        assertEquals("User not found with email: jems007patel@gmail.com", exception.getMessage());
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any(UserDetails.class));

        logger.info("Test completed: Login with Non-existing User");
    }

    @Test
    void shouldThrowException_WhenUserNotRegistered_AndResetPasswordCalled() {
        // Given
        logger.info("Starting test: Reset password without registering");
        String email = "dummy@gmail.com";
        String password = "12345678";
        logger.info("Will return false when trying to check if any email exists");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // When
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.resetPassword(email, password)
        );

        // Then
        logger.info("Caught user not found exception for {}", email);
        assertEquals(exception.getMessage(), "User not found with email: " + email);
        verify(passwordEncoder, never()).encode(password);
        verify(userRepository, never()).updatePassword(email, password);
        logger.info("Test completed: Reset password without registering");
    }

    @Test
    void shouldUpdatePassword_WhenUserIsRegistered() {
        // Given
        logger.info("Starting test: Reset password with user registered");
        String email = "dummy@gmail.com";
        String password = "12345678";
        String encodedPassword = "axbyhugjdigjalnge";
        logger.info("Will return true when trying to check if any email exists");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        logger.info("Will return encoded password ({}) when trying to encode password", encodedPassword);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // When
        userService.resetPassword(email, password);

        // Then
        verify(passwordEncoder).encode(password);
        verify(userRepository).updatePassword(email, encodedPassword);
        logger.info("Test completed: Reset password with user registered");
    }
}
