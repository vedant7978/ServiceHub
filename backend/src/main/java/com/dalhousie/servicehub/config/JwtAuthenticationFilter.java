package com.dalhousie.servicehub.config;

import com.dalhousie.servicehub.factory.service.ServiceFactory;
import com.dalhousie.servicehub.service.blacklist_token.BlackListTokenService;
import com.dalhousie.servicehub.service.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final int accessTokenStartInd = 7;

    private final JwtService jwtService;
    private final BlackListTokenService blackListTokenService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(ServiceFactory serviceFactory) {
        jwtService = serviceFactory.getJwtService();
        userDetailsService = serviceFactory.getUserDetailsService();
        blackListTokenService = serviceFactory.getBlackListTokenService();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwtToken = getJwtToken(request, response, filterChain);
        if (jwtToken == null || validateIfBlacklistTokenExists(jwtToken, response))
            return;

        String userEmail = getUserEmail(jwtToken, response);
        if (userEmail == null)
            return;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtToken(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return null;
        }
        return authHeader.substring(accessTokenStartInd);
    }

    private boolean validateIfBlacklistTokenExists(String jwtToken, HttpServletResponse response) throws IOException {
        if (!blackListTokenService.doesBlackListTokenExists(jwtToken))
            return false;
        // Token is invalid, set response status code and send a message
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("User is logged out.");
        return true;
    }

    private String getUserEmail(String jwtToken, HttpServletResponse response) throws IOException {
        // extract userEmail from jwtToken also catch error if the token is INVALID or EXPIRED
        try {
            return jwtService.extractUsername(jwtToken);
        } catch (ExpiredJwtException | MalformedJwtException e) {
            // Token has expired or malFormed, set response status code and send a message
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("User is Unauthorized. -> " + e.getMessage());
            return null;
        }
    }
}
