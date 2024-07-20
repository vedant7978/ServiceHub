package com.dalhousie.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ContractFeedbackDto {
    private Long contractId;
    private double rating;
    private String description;
}
