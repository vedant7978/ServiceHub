package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.model.ContractFeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractFeedbackRepository extends JpaRepository<ContractFeedbackModel, Long> {
    Optional<List<ContractFeedbackModel>> findAllByContractId(Long contractId);
}
