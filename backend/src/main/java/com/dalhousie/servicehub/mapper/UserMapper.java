package com.dalhousie.servicehub.mapper;

import com.dalhousie.servicehub.dto.UserDto;
import com.dalhousie.servicehub.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<UserModel, UserDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserDto toDto(UserModel userModel) {
        return modelMapper.map(userModel, UserDto.class);
    }

    @Override
    public UserModel toEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserModel.class);
    }
}
