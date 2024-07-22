package com.dalhousie.servicehub.response;

import lombok.Builder;
import lombok.Data;
import com.dalhousie.servicehub.enums.ServiceType;

@Data
@Builder
public class GetWishlistResponse {
    private Long id;
    private Long serviceId;
    private Long providerId;
    private String serviceProviderImage;
    private String name;
    private ServiceType type;
    private String description;
    private Double serviceProviderRating;
    private Double perHourRate;
}
