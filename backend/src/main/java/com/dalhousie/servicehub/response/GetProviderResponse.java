package com.dalhousie.servicehub.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetProviderResponse {
    private Long id;
    private String name;
    private String image;
    private String email;
    private String phone;
}
