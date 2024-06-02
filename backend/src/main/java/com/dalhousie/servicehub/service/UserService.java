package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.util.AuthenticationRequest;
import com.dalhousie.servicehub.util.AuthenticationResponse;

public interface UserService {
    public AuthenticationResponse register(UserModel request);
    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
