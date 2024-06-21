package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.model.BlackListTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackListTokenModel, Long> {
    Optional<BlackListTokenModel> findByToken(String token);
}
