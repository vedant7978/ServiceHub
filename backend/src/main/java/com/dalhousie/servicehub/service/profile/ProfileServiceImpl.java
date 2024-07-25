package com.dalhousie.servicehub.service.profile;

import com.dalhousie.servicehub.exceptions.PasswordNotMatchingException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.UpdateUserRequest;
import com.dalhousie.servicehub.response.UserDetailsResponse;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseBody<UserDetailsResponse> getUserDetailsResponse(UserModel userModel) {
        UserDetailsResponse userDetailsResponse = getUserDetailResponse(userModel);
        return new ResponseBody<>(SUCCESS, userDetailsResponse, "Fetched user details");
    }

    @Override
    public ResponseBody<UserDetailsResponse> updateUser(UpdateUserRequest updateUserRequest) {
        UserModel userModel = repository.findByEmail(updateUserRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + updateUserRequest.getEmail()));

        if (updateUserRequest.getName() != null)
            userModel.setName(updateUserRequest.getName());
        if (updateUserRequest.getPhone() != null)
            userModel.setPhone(updateUserRequest.getPhone());
        if (updateUserRequest.getAddress() != null)
            userModel.setAddress(updateUserRequest.getAddress());
        if (updateUserRequest.getImage() != null)
            userModel.setImage(updateUserRequest.getImage());

        repository.save(userModel);
        UserDetailsResponse userDetailsResponse = getUserDetailResponse(userModel);
        return new ResponseBody<>(SUCCESS, userDetailsResponse, "User updated successfully");
    }

    @Override
    public ResponseBody<String> newPassword(Long userId, String oldPassword, String newPassword) {
        UserModel existingUser = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (!passwordEncoder.matches(oldPassword, existingUser.getPassword()))
            throw new PasswordNotMatchingException("Old password not matching!");

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        repository.save(existingUser);
        return new ResponseBody<>(SUCCESS, "", "Password reset successfully");
    }

    /**
     * Convert the UserModel into UserDetailsResponse
     * @param userModel UserModel instance to convert
     * @return UserDetailsResponse instance
     */
    private UserDetailsResponse getUserDetailResponse(UserModel userModel) {
        return UserDetailsResponse.builder()
                .id(userModel.getId())
                .name(userModel.getName())
                .email(userModel.getEmail())
                .address(userModel.getAddress())
                .phone(userModel.getPhone())
                .image(userModel.getImage())
                .build();
    }
}