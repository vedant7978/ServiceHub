package com.dalhousie.servicehub.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsResponse {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String image;
}
