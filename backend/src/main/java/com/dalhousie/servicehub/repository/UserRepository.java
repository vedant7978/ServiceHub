package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET password = ?2 WHERE email = ?1", nativeQuery = true)
    void updatePassword(String email, String password);
}
