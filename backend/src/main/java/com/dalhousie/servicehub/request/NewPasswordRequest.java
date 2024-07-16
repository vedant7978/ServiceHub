package com.dalhousie.servicehub.request;

import lombok.Data;

@Data
public class NewPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
