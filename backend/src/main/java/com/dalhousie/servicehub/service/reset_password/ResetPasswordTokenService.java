package com.dalhousie.servicehub.service.reset_password;

import com.dalhousie.servicehub.model.ResetPasswordTokenModel;

import java.util.Optional;

public interface ResetPasswordTokenService {
    ResetPasswordTokenModel createResetPasswordToken(Long userId);

    void deleteResetPasswordToken(ResetPasswordTokenModel resetPasswordToken);

    Optional<ResetPasswordTokenModel> findByUserId(Long userId);

    boolean isTokenValid(ResetPasswordTokenModel token);
}
