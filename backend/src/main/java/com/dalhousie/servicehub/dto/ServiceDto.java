package com.dalhousie.servicehub.dto;

import com.dalhousie.servicehub.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
