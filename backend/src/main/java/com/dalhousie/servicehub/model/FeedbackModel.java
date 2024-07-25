package com.dalhousie.servicehub.model;

import com.dalhousie.servicehub.enums.FeedbackType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    private UserModel consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private UserModel provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type")
    private FeedbackType type;

    @OneToOne(mappedBy = "feedback", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ContractFeedbackModel contractFeedbackModel;
}
