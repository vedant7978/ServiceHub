package com.dalhousie.servicehub.util;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class forgotPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
}
