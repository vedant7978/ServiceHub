package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.model.RefreshTokenModel;
import com.dalhousie.servicehub.model.UserModel;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshTokenModel createRefreshToken(UserModel user);

    Optional<RefreshTokenModel> findByToken(String token);

    RefreshTokenModel verifyExpiration(RefreshTokenModel token);
}
