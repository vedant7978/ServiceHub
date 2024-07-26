package com.dalhousie.servicehub.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    /**
     * Extracts the username (email in our case) from the jwt token
     * @param jwtToken Token to extract username (email) from
     * @return String representing the username (email)
     */
    String extractUsername(String jwtToken);

    /**
     * Generates a token from provided UserDetails
     * @param userDetails UserDetails instance ot create token from
     * @return String representing the token
     */
    String generateToken(UserDetails userDetails);

    /**
     * Checks if the token is valid or not
     * @param token Token ot check validity
     * @param userDetails UserDetails instance to check token
     * @return True if token is valid, False otherwise
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
