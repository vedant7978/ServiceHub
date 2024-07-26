package com.dalhousie.servicehub.dto;

import com.dalhousie.servicehub.enums.ServiceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private Long id;
    private String description;
    private String name;
    private Double perHourRate;
    private ServiceType type;
    private Long providerId;
    private String providerImage;
    private boolean isAddedToWishlist;
    private boolean isRequested;
    private Double averageRating;
    private List<FeedbackDto> feedbacks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
