package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.request.AuthenticationRequest;
import com.dalhousie.servicehub.request.RegisterRequest;
import com.dalhousie.servicehub.response.AuthenticationResponse;

public interface UserService {
    /**
     * Register the user into the database
     * @param registerRequest Request body for the registration
     * @return Authentication response object on successful registration
     */
    AuthenticationResponse registerUser(RegisterRequest registerRequest);

    /**
     * Authenticates the user based on the credentials provided
     * @param authenticationRequest Request body for the authentication
     * @return Authentication response object on successful authentication
     */
    AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest);

    /**
     * Resets the password for the requesting email
     * @param email Email address to update password
     * @param password New password to update
     */
    void resetPassword(String email, String password);
}
