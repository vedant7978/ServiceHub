package com.dalhousie.servicehub.config;

import com.dalhousie.servicehub.service.JwtService;
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
        verify(filterChain, times(1)).doFilter(request, response);
        logger.info("Test completed: When no Authorization header, continue the filter chain");
    }

    @Test
    @DisplayName("When invalid Authorization header, continue the filter chain")
    void whenInvalidAuthHeader_thenContinueChain() throws IOException, ServletException {
        logger.info("Starting test: When invalid Authorization header, continue the filter chain");
        when(request.getHeader("Authorization")).thenReturn("Invalid Bearer token");
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        logger.info("Test completed: When invalid Authorization header, continue the filter chain");
    }

    @Test
    @DisplayName("Should extract username from JWT when header is valid")
    public void shouldExtractUsernameFromJwtWhenHeaderIsValid() throws Exception {
        logger.info("Starting test: Should extract username from JWT when header is valid");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("validUser");
        when(userDetailsService.loadUserByUsername("validUser")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).extractUsername("validToken");
        verify(filterChain, times(1)).doFilter(request, response);
        logger.info("Test completed: Should extract username from JWT when header is valid");
    }

    @Test
    @DisplayName("Should not extract username from JWT when header is invalid")
    public void shouldNotExtractUsernameFromJwtWhenHeaderIsInvalid() throws Exception {
        logger.info("Starting test: Should not extract username from JWT when header is invalid");
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(0)).extractUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        logger.info("Test completed: Should not extract username from JWT when header is invalid");
    }
}
