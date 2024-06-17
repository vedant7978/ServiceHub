package com.dalhousie.servicehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingContractDto {
    private Long id;
    private String address;
    private String serviceName;
    private String userImageUrl;
    private String userName;
    private Double userRating;
    private List<FeedbackDto> feedbacks;
}
