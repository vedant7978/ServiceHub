package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.model.ContractModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<ContractModel, Long> {

    @Query("SELECT c FROM ContractModel c WHERE c.service.id IN :serviceIds AND c.status = 'Pending' ORDER BY c.createdAt DESC")
    List<ContractModel> findPendingContractsByServiceIds(List<Long> serviceIds);

    @Query("SELECT c FROM ContractModel c WHERE c.service.id IN :serviceIds OR c.user.id = :userId ORDER BY c.createdAt DESC")
    List<ContractModel> findHistoryContractsByServiceIds(List<Long> serviceIds, Long userId);
}
