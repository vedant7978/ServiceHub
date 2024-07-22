package com.dalhousie.servicehub.service.reset_password;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.ResetPasswordTokenModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ResetPasswordTokenRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final UserRepository userRepository;

    @Override
    public ResetPasswordTokenModel createResetPasswordToken(Long userId) {
        resetPasswordTokenRepository.findByUserId(userId)
                .ifPresent(resetPasswordTokenRepository::delete);
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));
        ResetPasswordTokenModel resetPasswordTokenModel = ResetPasswordTokenModel.builder()
                .user(user)
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
    public boolean isTokenValid(ResetPasswordTokenModel resetPasswordTokenModel) {
        return resetPasswordTokenModel.getExpiryDate().compareTo(Instant.now()) >= 0;
    }
}
