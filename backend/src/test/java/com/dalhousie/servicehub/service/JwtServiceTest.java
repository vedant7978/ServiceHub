package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.service.jwt.JwtServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private static final Logger logger = LogManager.getLogger(JwtServiceTest.class);

    private JwtServiceImpl jwtService;
    private String expiredToken;

    /**
     * Do not directly use this variable, instead call {@link #getDummyToken()} method
     * to avoid creating new tokens everytime and use single valid token
     * throughout all test cases of this class
     */
    private String dummyToken = null;

    private final UserModel userDetails = UserModel.builder()
            .id(1L)
            .name("Vraj Shah")
            .email("vrajshah@gmail.com")
            .password("vrajshah")
            .image("www.placeholder.com")
            .phone("1234567890")
            .address("Altamount Road, Cumballa Hill, Mumbai")
            .build();

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        expiredToken = "eyJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiJ2cmFqc2hhaEBnbWFpbC5jb20iLCJpYXQiOjE3MTg2ODA0MTksImV4cCI6MTcxODY4MDQxOX0." +
                "1OnQfr3Fk1eimFxaR3L9_BOmSOelZ9BM7mjWeLGAF6U";
    }

    private String getDummyToken() {
        if (dummyToken == null)
            dummyToken = jwtService.generateToken(userDetails);
        return dummyToken;
    }

    @Test
    public void shouldGenerateTokenFromUserDetails() {
        // Given & When
        logger.info("Test started: shouldGenerateTokenFromUserDetails");
        String token = jwtService.generateToken(userDetails);

        // Then
        logger.info("Generated token: {}", token);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
        logger.info("Test completed: shouldGenerateTokenFromUserDetails");
    }

    @Test
    public void shouldExtractUsernameFromToken() {
        // Given
        logger.info("Test started: shouldExtractUsernameFromToken");
        String token = getDummyToken();

        // When
        logger.info("Passing token {} to extract username from", token);
        String email = jwtService.extractUsername(token);

        // Then
        logger.info("Received email: {}", email);
        assertEquals(userDetails.getEmail(), email);
        logger.info("Test completed: shouldExtractUsernameFromToken");
    }

    @Test
    public void shouldValidateIfTokenIsValidOrNot() {
        // Given
        logger.info("Test started: shouldValidateIfTokenIsValidOrNot");
        String validToken = getDummyToken();
        String invalidToken = "this.is-invalid.token";

        // When & Then
        logger.info("Passing valid token: {}", validToken);
        boolean isTokenValid1 = jwtService.isTokenValid(validToken, userDetails);
        assertTrue(isTokenValid1);

        // When & Then
        logger.info("Passing invalid token: {}", invalidToken);
        boolean isTokenValid2 = jwtService.isTokenValid(invalidToken, userDetails);
        assertFalse(isTokenValid2);

        // When & Then
        logger.info("Passing expired token: {}", expiredToken);
        boolean isTokenValid3 = jwtService.isTokenValid(expiredToken, userDetails);
        assertFalse(isTokenValid3);
        logger.info("Test completed: shouldValidateIfTokenIsValidOrNot");
    }
}
