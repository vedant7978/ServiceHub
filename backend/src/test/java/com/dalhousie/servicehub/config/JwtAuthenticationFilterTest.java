package com.dalhousie.servicehub.config;

import com.dalhousie.servicehub.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {
    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilterTest.class);

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When no Authorization header, continue the filter chain")
    void whenNoAuthHeader_thenContinueChain() throws IOException, ServletException {
        logger.info("Starting test: When no Authorization header, continue the filter chain");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        logger.info("No Authorization header present in request");
        verify(filterChain, times(1)).doFilter(request, response);

        logger.info("Test completed: When no Authorization header, continue the filter chain");
    }

    @Test
    @DisplayName("When invalid Authorization header, continue the filter chain")
    void whenInvalidAuthHeader_thenContinueChain() throws IOException, ServletException {
        logger.info("Starting test: When invalid Authorization header, continue the filter chain");

        String invalidHeader = "Invalid Bearer token";
        when(request.getHeader("Authorization")).thenReturn(invalidHeader);
        logger.info("Authorization header: {}", invalidHeader);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        logger.info("Invalid Authorization header detected");
        verify(filterChain, times(1)).doFilter(request, response);

        logger.info("Test completed: When invalid Authorization header, continue the filter chain");
    }

    @Test
    @DisplayName("Should extract username from JWT when header is valid")
    public void shouldExtractUsernameFromJwtWhenHeaderIsValid() throws Exception {
        logger.info("Starting test: Should extract username from JWT when header is valid");

        String validToken = "validToken";
        String validUser = "validUser";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.extractUsername("validToken")).thenReturn(validUser);
        when(userDetailsService.loadUserByUsername("validUser")).thenReturn(null);

        logger.info("Authorization header: Bearer {}", validToken);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        logger.info("Extracted username from JWT: {}", validUser);
        verify(jwtService, times(1)).extractUsername(validToken);
        verify(filterChain, times(1)).doFilter(request, response);

        logger.info("Test completed: Should extract username from JWT when header is valid");
    }

    @Test
    @DisplayName("Should not extract username from JWT when header is invalid")
    public void shouldNotExtractUsernameFromJwtWhenHeaderIsInvalid() throws Exception {
        logger.info("Starting test: Should not extract username from JWT when header is invalid");

        String invalidHeader = "InvalidHeader";
        when(request.getHeader("Authorization")).thenReturn(invalidHeader);
        logger.info("Authorization header: {}", invalidHeader);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        logger.info("Invalid Authorization header detected, no username extraction performed");
        verify(jwtService, times(0)).extractUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        logger.info("Test completed: Should not extract username from JWT when header is invalid");
    }
}
