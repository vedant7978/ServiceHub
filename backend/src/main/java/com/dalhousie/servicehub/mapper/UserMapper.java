package com.dalhousie.servicehub.mapper;

import com.dalhousie.servicehub.dto.UserDto;
import com.dalhousie.servicehub.model.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserModel, UserDto> {


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto toDto(UserModel userModel) {
        return modelMapper.map(userModel, UserDto.class);
    }

    @Override
    public UserModel toEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserModel.class);
    }
}
