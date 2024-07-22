package com.dalhousie.servicehub.dto;

import com.dalhousie.servicehub.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDto {
    private String providerName;
    private double rating;
    private String description;
    private FeedbackType type;
}
