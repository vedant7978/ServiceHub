package com.dalhousie.servicehub.service.reset_password;

import com.dalhousie.servicehub.model.ResetPasswordTokenModel;
import com.dalhousie.servicehub.repository.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Override
    public ResetPasswordTokenModel createResetPasswordToken(Long userId) {
        resetPasswordTokenRepository.findByUserId(userId)
                .ifPresent(resetPasswordTokenRepository::delete);
        ResetPasswordTokenModel resetPasswordTokenModel = ResetPasswordTokenModel.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(1000L * 60 * 10)) // 10 Minutes
                .build();
        return resetPasswordTokenRepository.save(resetPasswordTokenModel);
    }

    @Override
    public void deleteResetPasswordToken(ResetPasswordTokenModel resetPasswordToken) {
        resetPasswordTokenRepository.delete(resetPasswordToken);
    }

    @Override
    public Optional<ResetPasswordTokenModel> findByUserId(Long userId) {
        return resetPasswordTokenRepository.findByUserId(userId);
    }

    @Override
    public boolean isTokenValid(ResetPasswordTokenModel token) {
        return token.getExpiryDate().compareTo(Instant.now()) >= 0;
    }
}
