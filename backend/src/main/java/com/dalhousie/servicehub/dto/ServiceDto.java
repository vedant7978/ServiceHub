package com.dalhousie.servicehub.dto;

import com.dalhousie.servicehub.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private Long id;
    private String description;
    private String name;
    private Double perHourRate;
    private ServiceType type;
    private Long providerId;
    private boolean isAddedToWishlist;
    private Double averageRating;
}
