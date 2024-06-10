package com.dalhousie.servicehub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "feedback")
public class FeedbackModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "rating", columnDefinition = "DECIMAL(4,2) DEFAULT 0.00")
    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.00", message = "Rating must be at least 0.00")
    @DecimalMax(value = "5.00", message = "Rating must be at most 5.00")
    private Double rating;

    @Column(name = "consumer_id")
    @NotNull(message = "Consumer ID is required")
    private Long consumerId;

    @Column(name = "provider_id")
    @NotNull(message = "Provider ID is required")
    private Long providerId;
}
