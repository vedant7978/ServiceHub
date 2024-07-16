package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.exceptions.PasswordNotMatchingException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.UpdateUserRequest;
import com.dalhousie.servicehub.service.profile.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SuppressWarnings("LoggingSimilarMessage")
@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceTest.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;
    private UserModel userModel;
    private UpdateUserRequest updateUserRequest;

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

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("jems007patel@gmail.com");
        updateUserRequest.setName("Updated Name");
        updateUserRequest.setPhone("9876543210");
        updateUserRequest.setAddress("Updated Address");
        updateUserRequest.setImage("updated-image.jpg");
    }

    @Test
    @DisplayName("Should return user when email exists")
    void getUserByEmail_WhenEmailExists() {
        logger.info("Starting test: getUserByEmail_WhenEmailExists");

        // Given
        logger.info("Mocking userRepository.findByEmail to return userModel");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userModel));

        // When
        UserModel foundUser = userService.getUserByEmail("jems007patel@gmail.com");

        // Then
        logger.info("Asserting that the foundUser is not null and has the correct email");
        assertNotNull(foundUser);
        assertEquals("jems007patel@gmail.com", foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(anyString());

        logger.info("Test completed: getUserByEmail_WhenEmailExists");
    }

    @Test
    @DisplayName("Should throw exception when email does not exist")
    void getUserByEmail_WhenEmailDoesNotExist() {
        logger.info("Starting test: getUserByEmail_WhenEmailDoesNotExist");

        // Given
        logger.info("Mocking userRepository.findByEmail to return empty Optional");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When
        logger.info("Expecting UserNotFoundException to be thrown");
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.getUserByEmail("unknown@gmail.com")
        );

        // Then
        logger.info("Asserting that the exception message is correct");
        assertEquals("User not found with email: unknown@gmail.com", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(anyString());

        logger.info("Test completed: getUserByEmail_WhenEmailDoesNotExist");
    }

    @Test
    @DisplayName("Should update user details when email exists")
    void updateUser_WhenEmailExists() {
        logger.info("Starting test: updateUser_WhenEmailExists");

        // Given
        logger.info("Mocking userRepository.findByEmail to return userModel");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userModel));
        logger.info("Mocking userRepository.save to return updated userModel");
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        // When
        UserModel updatedUser = userService.updateUser(updateUserRequest);

        // Then
        logger.info("Asserting that the updatedUser is not null and fields are updated");
        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("9876543210", updatedUser.getPhone());
        assertEquals("Updated Address", updatedUser.getAddress());
        assertEquals("updated-image.jpg", updatedUser.getImage());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(UserModel.class));

        logger.info("Test completed: updateUser_WhenEmailExists");
    }

    @Test
    @DisplayName("Should throw exception when updating and email does not exist")
    void updateUser_WhenEmailDoesNotExist() {
        logger.info("Starting test: updateUser_WhenEmailDoesNotExist");

        // Given
        logger.info("Mocking userRepository.findByEmail to return empty Optional");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When
        logger.info("Expecting UserNotFoundException to be thrown");
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.updateUser(updateUserRequest)
        );

        // Then
        logger.info("Asserting that the exception message is correct");
        assertEquals("User not found with email: jems007patel@gmail.com", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(UserModel.class));

        logger.info("Test completed: updateUser_WhenEmailDoesNotExist");
    }

    @Test
    @DisplayName("Should not update user details when email exists and update request has no data")
    void doNotUpdateUser_WhenEmailExists_AndNoDataToUpdate() {
        logger.info("Starting test: doNotUpdateUser_WhenEmailExists_AndNoDataToUpdate");

        // Given
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail(userModel.getEmail());
        logger.info("Mocking userRepository.findByEmail to return userModel");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userModel));
        logger.info("Mocking userRepository.save to return updated userModel");
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        // When
        UserModel updatedUser = userService.updateUser(request);

        // Then
        logger.info("Asserting that the updatedUser is not null and fields are updated");
        assertNotNull(updatedUser);
        assertEquals(userModel.getName(), updatedUser.getName());
        assertEquals(userModel.getPhone(), updatedUser.getPhone());
        assertEquals(userModel.getAddress(), updatedUser.getAddress());
        assertEquals(userModel.getImage(), updatedUser.getImage());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(UserModel.class));

        logger.info("Test completed: doNotUpdateUser_WhenEmailExists_AndNoDataToUpdate");
    }

    @Test
    @DisplayName("Should reset password successfully when user exists")
    void newPassword_WhenUserExists() {
        logger.info("Starting test: resetPassword_WhenUserExists");

        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(userModel));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // When
        userService.newPassword(1L, "oldPassword", "newPassword");

        // Then
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(UserModel.class));
        verify(passwordEncoder, times(1)).encode("newPassword");

        logger.info("Test completed: resetPassword_WhenUserExists");
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void newPassword_WhenUserDoesNotExist() {
        logger.info("Starting test: resetPassword_WhenUserDoesNotExist");

        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.newPassword(1L, "", "newPassword")
        );

        // Then
        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(UserModel.class));

        logger.info("Test completed: resetPassword_WhenUserDoesNotExist");
    }

    @Test
    @DisplayName("Should throw PasswordNotMatchingException when old password does not match")
    void newPassword_WhenPasswordNotMatching() {
        logger.info("Starting test: resetPassword_WhenPasswordNotMatching");

        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(userModel));
        logger.info("Will return user with ID: {} when findById is called", userModel.getId());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        logger.info("Will return false matching old password with encoded password");

        // When
        PasswordNotMatchingException exception = assertThrows(PasswordNotMatchingException.class, () ->
                userService.newPassword(1L, "", "newPassword")
        );

        // Then
        assertEquals("Old password not matching!", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(UserModel.class));

        logger.info("Test completed: resetPassword_WhenPasswordNotMatching");
    }
}