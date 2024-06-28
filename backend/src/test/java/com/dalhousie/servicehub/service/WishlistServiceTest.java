package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.dto.WishlistDto;
import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.mapper.WishlistMapper;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.model.WishlistModel;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.repository.WishlistRepository;
import com.dalhousie.servicehub.response.GetWishlistResponse;
import com.dalhousie.servicehub.service.wishlist.WishlistServiceImpl;
import com.dalhousie.servicehub.util.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WishlistMapper wishlistMapper;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private UserModel userModel;

    @BeforeEach
    void setUp() {
        userModel = UserModel.builder()
                .id(1L)
                .name("Test User")
                .email("testuser@example.com")
                .password("testPassword")
                .build();
    }

    @Test
    @DisplayName("Add Wishlist Successfully")
    void addWishlist_Success() {
        // Given
        Long serviceId = 1L;
        WishlistModel wishlistModel = WishlistModel.builder()
                .serviceId(serviceId)
                .userId(userModel.getId())
                .build();

        when(serviceRepository.existsById(serviceId)).thenReturn(true);
        when(wishlistRepository.save(any(WishlistModel.class))).thenReturn(wishlistModel);

        // When
        ResponseBody<String> response = wishlistService.addWishlist(serviceId, userModel);

        // Then
        verify(wishlistRepository, times(1)).save(any(WishlistModel.class));
    }

    @Test
    @DisplayName("Add Wishlist Service Not Found")
    void addWishlist_ServiceNotFound() {
        // Given
        Long serviceId = 1L;

        when(serviceRepository.existsById(serviceId)).thenReturn(false);

        // When & Then
        assertThrows(ServiceNotFoundException.class, () -> wishlistService.addWishlist(serviceId, userModel));
        verify(wishlistRepository, never()).save(any(WishlistModel.class));
    }

    @Test
    @DisplayName("Get Wishlists Successfully")
    void getWishlists_Success() {
        // Given
        Long userId = 1L;
        WishlistModel wishlistModel = WishlistModel.builder()
                .id(1L)
                .serviceId(1L)
                .userId(userId)
                .build();

        List<WishlistModel> wishlistModels = new ArrayList<>();
        wishlistModels.add(wishlistModel);

        List<WishlistDto> wishlistDtos = new ArrayList<>();
        wishlistDtos.add(WishlistDto.builder().id(1L).serviceId(1L).build());

        GetWishlistResponse expectedResponse = GetWishlistResponse.builder()
                .wishlists(wishlistDtos)
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(wishlistRepository.findAllByUserId(userId)).thenReturn(wishlistModels);
        when(wishlistMapper.toDto(wishlistModel)).thenReturn(WishlistDto.builder().id(1L).serviceId(1L).build());

        // When
        ResponseBody<GetWishlistResponse> response = wishlistService.getWishlists(userId);

        // Then
        verify(wishlistRepository, times(1)).findAllByUserId(userId);
        verify(wishlistMapper, times(1)).toDto(wishlistModel);
    }

    @Test
    @DisplayName("Get Wishlists User Not Found")
    void getWishlists_UserNotFound() {
        // Given
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> wishlistService.getWishlists(userId));
        verify(wishlistRepository, never()).findAllByUserId(anyLong());
        verify(wishlistMapper, never()).toDto(any(WishlistModel.class));
    }
}