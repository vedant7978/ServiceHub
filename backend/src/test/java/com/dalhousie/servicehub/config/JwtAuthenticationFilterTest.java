package com.dalhousie.servicehub.config;

import com.dalhousie.servicehub.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("When invalid Authorization header, continue the filter chain")
    void whenInvalidAuthHeader_thenContinueChain() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Invalid Bearer token");
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should extract username from JWT when header is valid")
    public void shouldExtractUsernameFromJwtWhenHeaderIsValid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("validUser");
        when(userDetailsService.loadUserByUsername("validUser")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(1)).extractUsername("validToken");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should not extract username from JWT when header is invalid")
    public void shouldNotExtractUsernameFromJwtWhenHeaderIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, times(0)).extractUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
