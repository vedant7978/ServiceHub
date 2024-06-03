package com.dalhousie.servicehub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String name;
    private String image;
    @Email(message = "Invalid email address")
    private String email;
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Invalid phone number format")
    private String phone;
    private String address;
    @Size(min = 8, message = "Password must have at least 8 characters!")
    private String password;

}
