package com.dalhousie.servicehub.service.profile;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.UpdateUserRequest;
import com.dalhousie.servicehub.response.UserDetailsResponse;
import com.dalhousie.servicehub.util.ResponseBody;

public interface ProfileService {

    /**
     * Provides UserDetailsResponse from provided UserModel
     * @param userModel UserModel instance to convert to UserDetailsResponse
     * @return ResponseBody object for UserDetailResponse
     */
    ResponseBody<UserDetailsResponse> getUserDetailsResponse(UserModel userModel);

    /**
     * Update a user with new details provided
     * @param updateUserRequest New data for the requesting user
     * @return ResponseBody object for UserDetailResponse
     */
    ResponseBody<UserDetailsResponse> updateUser(UpdateUserRequest updateUserRequest);

    /**
     * Set new password for the requesting user id
     * @param userId ID of the user
     * @param oldPassword Old password that was set
     * @param newPassword New password to set
     * @return ResponseBody object for String representing api result
     */
    ResponseBody<String> newPassword(Long userId, String oldPassword, String newPassword);
}
