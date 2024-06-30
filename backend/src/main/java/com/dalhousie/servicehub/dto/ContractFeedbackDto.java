package com.dalhousie.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractFeedbackDto {
    private Long contractId;
    private double rating;
    private String description;
}
