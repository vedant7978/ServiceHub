package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.ResetPasswordTokenModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ResetPasswordTokenRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.service.reset_password.ResetPasswordTokenServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("LoggingSimilarMessage")
@ExtendWith(MockitoExtension.class)
public class ResetPasswordTokenServiceTest {

    private static final Logger logger = LogManager.getLogger(ResetPasswordTokenServiceTest.class);

    @Mock
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ResetPasswordTokenServiceImpl resetPasswordTokenService;

    private final UserModel userModel = UserModel.builder()
            .id(10L)
            .build();

    @Test
    void shouldCreateResetPasswordToken_WhenUserIdDoesNotExist() {
        // Given
        logger.info("Test started: shouldCreateResetPasswordToken_WhenUserIdDoesNotExist");
        Long userId = 10L;
        ArgumentCaptor<ResetPasswordTokenModel> captor = ArgumentCaptor.forClass(ResetPasswordTokenModel.class);
        logger.info("Will return null model when finding user with specific id");
        when(resetPasswordTokenRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));

        // When
        resetPasswordTokenService.createResetPasswordToken(userId);

        // Then
        verify(resetPasswordTokenRepository).save(captor.capture());
        logger.info("Captured model to save in database: {}", captor.getValue());
        verify(resetPasswordTokenRepository, never()).delete(any());
        assertEquals(userId, captor.getValue().getUser().getId());
        logger.info("Test completed: shouldCreateResetPasswordToken_WhenUserIdDoesNotExist");
    }

    @Test
    void shouldCreateResetPasswordToken_WhenUserIdAlreadyExist() {
        // Given
        logger.info("Test started: shouldCreateResetPasswordToken_WhenUserIdAlreadyExist");
        Long userId = 10L;
        String oldToken = "oldToken";
        ArgumentCaptor<ResetPasswordTokenModel> captor = ArgumentCaptor.forClass(ResetPasswordTokenModel.class);
        ResetPasswordTokenModel dummyModel = ResetPasswordTokenModel.builder().user(userModel).token(oldToken).build();
        logger.info("Will return dummy model when finding user with specific id: {}", dummyModel);
        when(resetPasswordTokenRepository.findByUserId(userId)).thenReturn(Optional.of(dummyModel));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));

        // When
        resetPasswordTokenService.createResetPasswordToken(userId);

        // Then
        verify(resetPasswordTokenRepository).save(captor.capture());
        logger.info("Captured model to save in database: {}", captor.getValue());
        verify(resetPasswordTokenRepository).delete(dummyModel);
        assertEquals(userId, captor.getValue().getUser().getId());
        assertNotEquals(oldToken, captor.getValue().getToken());
        logger.info("Test completed: shouldCreateResetPasswordToken_WhenUserIdAlreadyExist");
    }

    @Test
    void shouldThrowException_WhenUserIdDoesNotExist() {
        // Given
        logger.info("Test started: shouldThrowException_WhenUserIdDoesNotExist");
        long userId = 10L;
        logger.info("Will return empty when trying to get user with id {}", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> resetPasswordTokenService.createResetPasswordToken(userId));

        // Then
        assertEquals("User not found for id: " + userId, exception.getMessage());
        verify(resetPasswordTokenRepository, never()).save(any());
        logger.info("Test completed: shouldThrowException_WhenUserIdDoesNotExist");
    }

    @Test
    void shouldDeleteResetPasswordTokenModel() {
        // Given
        logger.info("Test started: shouldDeleteResetPasswordTokenModel");
        ResetPasswordTokenModel dummyModel = ResetPasswordTokenModel.builder().build();

        // When
        logger.info("Deleting model from database: {}", dummyModel);
        resetPasswordTokenService.deleteResetPasswordToken(dummyModel);

        // Then
        verify(resetPasswordTokenRepository).delete(dummyModel);
        logger.info("Test completed: shouldDeleteResetPasswordTokenModel");
    }

    @Test
    void shouldReturnResetPasswordTokenModel_WhenFindByTokenIsCalled() {
        // Given
        logger.info("Test started: shouldReturnResetPasswordTokenModel_WhenFindByUserIdIsCalled");
        Long userId = 10L;
        ResetPasswordTokenModel dummyModel = ResetPasswordTokenModel.builder().user(userModel).build();
        when(resetPasswordTokenRepository.findByUserId(userId)).thenReturn(Optional.of(dummyModel));

        // When
        logger.info("Getting model from database with userId: {}", userId);
        Optional<ResetPasswordTokenModel> result = resetPasswordTokenService.findByUserId(userId);

        // Then
        verify(resetPasswordTokenRepository).findByUserId(userId);
        assertTrue(result.isPresent());
        assertEquals(result.get().getUser().getId(), userId);
        logger.info("Test completed: shouldReturnResetPasswordTokenModel_WhenFindByUserIdIsCalled");
    }

    @Test
    void shouldReturnFalse_WhenTokenIsInvalid_AndIsTokenValidCalled() {
        // Given
        logger.info("Test started: shouldReturnFalse_WhenTokenIsInvalid_AndIsTokenValidCalled");
        Long userId = 10L;
        ResetPasswordTokenModel dummyModel = ResetPasswordTokenModel.builder()
                .user(userModel)
                .expiryDate(Instant.now().minusMillis(1000L)) // expired before 1000 milliseconds
                .build();

        // When
        logger.info("Getting model from database with userId: {}", userId);
        boolean result = resetPasswordTokenService.isTokenValid(dummyModel);

        // Then
        assertFalse(result);
        logger.info("Test completed: shouldReturnFalse_WhenTokenIsInvalid_AndIsTokenValidCalled");
    }

    @Test
    void shouldReturnTrue_WhenTokenIsValid_AndIsTokenValidCalled() {
        // Given
        logger.info("Test started: shouldReturnTrue_WhenTokenIsValid_AndIsTokenValidCalled");
        Long userId = 10L;
        ResetPasswordTokenModel dummyModel = ResetPasswordTokenModel.builder()
                .user(userModel)
                .expiryDate(Instant.now().plusMillis(1000L)) // expiring after 1000 milliseconds
                .build();

        // When
        logger.info("Getting model from database with userId: {}", userId);
        boolean result = resetPasswordTokenService.isTokenValid(dummyModel);

        // Then
        assertTrue(result);
        logger.info("Test completed: shouldReturnTrue_WhenTokenIsValid_AndIsTokenValidCalled");
    }
}
