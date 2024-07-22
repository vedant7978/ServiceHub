package com.dalhousie.servicehub.request;

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
public class ContractRequest {

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotBlank(message = "Address is required")
    private String address;
}

