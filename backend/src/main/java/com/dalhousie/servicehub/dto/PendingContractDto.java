package com.dalhousie.servicehub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PendingContractDto {
    private Long id;
    private String address;
    private String serviceName;
    private String serviceProviderName;
    private Double perHourRate;
    private String userImageUrl;
    private String userName;
    private Double userRating;
    private List<FeedbackDto> feedbacks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
