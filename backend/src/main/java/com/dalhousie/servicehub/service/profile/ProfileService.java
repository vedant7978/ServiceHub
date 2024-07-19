package com.dalhousie.servicehub.service.profile;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.UpdateUserRequest;
import com.dalhousie.servicehub.response.UserDetailsResponse;

public interface ProfileService {

    /**
     * Update a user with new details provided
     * @param updateUserRequest New data for the requesting user
     * @return UserModel instance with the updated values
     */
    UserDetailsResponse updateUser(UpdateUserRequest updateUserRequest);

    /**
     * Set new password for the requesting user id
     * @param userId ID of the user
     * @param oldPassword Old password that was set
     * @param newPassword New password to set
     */
    void newPassword(Long userId, String oldPassword, String newPassword);

    /**
     * Provides UserDetailsResponse from provided UserModel
     * @param userModel UserModel instance to convert to UserDetailsResponse
     * @return UserDetailsResponse object
     */
    UserDetailsResponse getUserDetailsResponse(UserModel userModel);
}
