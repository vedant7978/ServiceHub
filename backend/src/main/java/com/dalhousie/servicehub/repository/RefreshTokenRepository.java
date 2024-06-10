package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.model.RefreshTokenModel;
import com.dalhousie.servicehub.model.UserModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenModel, Long> {
    Optional<RefreshTokenModel> findByToken(String token);
    Optional<RefreshTokenModel> findByUserInfo(Optional<UserModel> user);

}