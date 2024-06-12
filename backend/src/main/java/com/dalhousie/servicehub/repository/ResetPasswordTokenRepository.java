package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.model.ResetPasswordTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordTokenModel, Long> {
    Optional<ResetPasswordTokenModel> findByUserId(Long userId);
}