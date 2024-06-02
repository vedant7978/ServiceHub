package com.dalhousie.servicehub.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    public String extractUsername(String jwtToken);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    public Key getSigningKey();

    public String generateToken(UserDetails userDetails);

    public String buildToken(Map<String,Object> extraClaims, String subject);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public boolean isTokenExpired(String token);

    public Date extractExpiration(String token);
}
