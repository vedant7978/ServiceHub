package com.dalhousie.servicehub.repository;

import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel, Long> {

    List<ServiceModel> findByProviderId(Long providerId);

    @Modifying
    @Transactional
    @Query("UPDATE ServiceModel s SET s.description = :description, s.name = :name, s.perHourRate = :perHourRate, s.type = :type WHERE s.id = :id AND s.provider = :provider")
    void updateService(Long id, String description, String name, Double perHourRate, ServiceType type, UserModel provider);

    @Query("SELECT s.id FROM ServiceModel s WHERE s.provider = :provider")
    List<Long> getServiceIdsByProviderId(UserModel provider);

    List<ServiceModel> findByType(ServiceType type);

    List<ServiceModel> findByNameContainingIgnoreCase(String name);
}