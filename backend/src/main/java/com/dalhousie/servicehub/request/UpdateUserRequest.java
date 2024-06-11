package com.dalhousie.servicehub.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Email
    @NotEmpty
    private String email;
    private String name;
    private String phone;
    private String address;
    private String image;
}
