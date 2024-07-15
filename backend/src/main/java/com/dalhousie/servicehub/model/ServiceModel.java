package com.dalhousie.servicehub.model;

import com.dalhousie.servicehub.enums.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "service")
public class ServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String name;

    @Column(name = "per_hour_rate")
    private Double perHourRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType type;

    @Column(name = "provider_id", nullable = false)
    private Long providerId;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<WishlistModel> wishlists;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ContractModel> contracts;
}