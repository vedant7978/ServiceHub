package com.dalhousie.servicehub.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFeedbackRequest {

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "Consumer ID is required")
    private Long consumerId;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.00", message = "Rating must be at least 0.00")
    @DecimalMax(value = "10.00", message = "Rating must be at most 10.00")
    private Double rating;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;
}
