package com.dalhousie.servicehub.response;

import lombok.Builder;
import lombok.Data;
import com.dalhousie.servicehub.enums.ServiceType;

@Data
@Builder
public class GetWishlistResponse {
    private Long serviceId;
    private Long providerId;
    private String serviceProviderImage;
    private String serviceName;
    private ServiceType serviceType;
    private String serviceDescription;
    private Double serviceProviderRating;
    private Double servicePerHourRate;
}
