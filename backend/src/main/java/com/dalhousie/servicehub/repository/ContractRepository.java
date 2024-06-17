package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.model.ContractModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<ContractModel, Long> {

    @Query("SELECT c FROM ContractModel c WHERE c.service.id IN :serviceIds AND c.isPending = true")
    List<ContractModel> findPendingContractsByServiceIds(List<Long> serviceIds);
}
