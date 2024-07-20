package com.dalhousie.servicehub.service.reset_password;

import com.dalhousie.servicehub.model.ResetPasswordTokenModel;

import java.util.Optional;

public interface ResetPasswordTokenService {

    /**
     * Creates a token for resetting password
     * @param userId ID of the user
     * @return ResetPasswordTokenModel instance
     */
    ResetPasswordTokenModel createResetPasswordToken(Long userId);

    /**
     * Deletes the provided token inside ResetPasswordTokenModel
     * @param resetPasswordToken ResetPasswordTokenModel instance containing token
     */
    void deleteResetPasswordToken(ResetPasswordTokenModel resetPasswordToken);

    /**
     * Provides the ResetPasswordTokenModel from user ID
     * @param userId ID of the user
     * @return Optional value for ResetPasswordTokenModel instance
     */
    Optional<ResetPasswordTokenModel> findByUserId(Long userId);

    /**
     * Checks if provided token is valid or not
     * @param resetPasswordTokenModel ResetPasswordTokenModel instance containing token
     * @return True if token is valid, False otherwise
     */
    boolean isTokenValid(ResetPasswordTokenModel resetPasswordTokenModel);
}
