package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.model.FeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Long> {
    Optional<List<FeedbackModel>> findAllByConsumerId(long consumerId);
}
