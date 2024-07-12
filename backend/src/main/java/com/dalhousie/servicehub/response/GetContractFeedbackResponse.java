package com.dalhousie.servicehub.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetContractFeedbackResponse {
    private double rating;
    private String description;
}
