package com.dalhousie.servicehub.mapper;

import com.dalhousie.servicehub.dto.WishlistDto;
import com.dalhousie.servicehub.model.WishlistModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper implements Mapper<WishlistModel, WishlistDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public WishlistDto toDto(WishlistModel wishlistModel) {
        return modelMapper.map(wishlistModel, WishlistDto.class);
    }

    @Override
    public WishlistModel toEntity(WishlistDto wishlistDto) {
        return modelMapper.map(wishlistDto, WishlistModel.class);
    }
}