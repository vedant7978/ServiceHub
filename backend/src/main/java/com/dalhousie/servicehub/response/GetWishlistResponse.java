package com.dalhousie.servicehub.response;

import com.dalhousie.servicehub.dto.ServiceDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetWishlistResponse {
    private List<ServiceDto> services;
}
