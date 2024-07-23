package com.dalhousie.servicehub.service.user;

import com.dalhousie.servicehub.request.AuthenticationRequest;
import com.dalhousie.servicehub.request.RegisterRequest;
import com.dalhousie.servicehub.request.ResetPasswordRequest;
import com.dalhousie.servicehub.response.AuthenticationResponse;
import com.dalhousie.servicehub.util.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    /**
     * Register the user into the database
     * @param registerRequest Request body for the registration
     * @return Response body object for Authentication response object on successful registration
     */
    ResponseBody<AuthenticationResponse> registerUser(RegisterRequest registerRequest);

    /**
     * Authenticates the user based on the credentials provided
     * @param authenticationRequest Request body for the authentication
     * @return Response body object for Authentication response object on successful authentication
     */
    ResponseBody<AuthenticationResponse> authenticateUser(AuthenticationRequest authenticationRequest);

    /**
     * Resets the password for the requesting email
     * @param resetPasswordRequest Request body for resetting password
     * @return Response body object for String representing api result
     */
    ResponseBody<String> resetPassword(ResetPasswordRequest resetPasswordRequest);

    /**
     * Called when user forgets the password and want to receive a reset password link
     * @param email Email of the registered user
     * @param servletRequest HttpServletRequest instance
     * @return Response body object for String representing api result
     */
    ResponseBody<String> forgotPassword(String email, HttpServletRequest servletRequest);

    /**
     * Signs out user from the application
     * @param token Jwt token of the currently logged-in user
     * @return Response body object for String representing api result
     */
    ResponseBody<String> signOut(String token);
}
