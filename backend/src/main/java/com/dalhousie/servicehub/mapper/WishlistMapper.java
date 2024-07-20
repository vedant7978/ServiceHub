package com.dalhousie.servicehub.mapper;

import com.dalhousie.servicehub.dto.WishlistDto;
import com.dalhousie.servicehub.model.WishlistModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistMapper implements Mapper<WishlistModel, WishlistDto> {

    private final ModelMapper modelMapper;

    @Override
    public WishlistDto toDto(WishlistModel wishlistModel) {
        return modelMapper.map(wishlistModel, WishlistDto.class);
    }

    @Override
    public WishlistModel toEntity(WishlistDto wishlistDto) {
        return modelMapper.map(wishlistDto, WishlistModel.class);
    }
}