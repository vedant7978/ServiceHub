package com.dalhousie.servicehub.util;

import com.dalhousie.servicehub.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated or no valid authentication found");
        }

        UserModel userModel = (UserModel) authentication.getPrincipal();
        return userModel.getId();
    }
}


