package com.dalhousie.servicehub.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateServiceRequest {

    @NotNull(message = "Service ID is required")
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Per hour rate is required")
    @DecimalMin(value = "0.00", message = "Per hour rate must be at least 0.00")
    private Double perHourRate;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Provider ID is required")
    private Long providerId;
}
