package com.dalhousie.servicehub.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
