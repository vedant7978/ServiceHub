package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.model.RefreshTokenModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.RefreshTokenRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public RefreshTokenModel createRefreshToken(UserModel user){
        Optional<RefreshTokenModel> existingToken = refreshTokenRepository.findByUserInfo(Optional.ofNullable(user));
        existingToken.ifPresent(refreshTokenRepository::delete);
        RefreshTokenModel refreshTokenModel = RefreshTokenModel.builder()
                .userInfo(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000)) // 10 minutes for example
                .build();
        return refreshTokenRepository.save(refreshTokenModel);
    }

    @Override
    public Optional<RefreshTokenModel> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenModel verifyExpiration(RefreshTokenModel token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

}
