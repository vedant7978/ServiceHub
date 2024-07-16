package com.dalhousie.servicehub.service.profile;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.UpdateUserRequest;

public interface ProfileService {

    UserModel getUserByEmail(String email);
    UserModel updateUser(UpdateUserRequest updateUserRequest);
    void newPassword(Long userId, String oldPassword, String newPassword);
}
