package com.dalhousie.servicehub.service.profile;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final UserRepository repository;


    @Override
    public UserModel getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public UserModel updateUser(UpdateUserRequest updateUserRequest) {
        UserModel userModel = repository.findByEmail(updateUserRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + updateUserRequest.getEmail()));

        if (updateUserRequest.getName() != null) {
            userModel.setName(updateUserRequest.getName());
        }
        if (updateUserRequest.getPhone() != null) {
            userModel.setPhone(updateUserRequest.getPhone());
        }
        if (updateUserRequest.getAddress() != null) {
            userModel.setAddress(updateUserRequest.getAddress());
        }
        if (updateUserRequest.getImage() != null) {
            userModel.setImage(updateUserRequest.getImage());
        }
        repository.save(userModel);
        return userModel;
    }
}
