package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.mapper.WishlistMapper;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.model.WishlistModel;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.repository.WishlistRepository;
import com.dalhousie.servicehub.response.GetWishlistResponse;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
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
import java.util.Optional;

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
    @Mock
    private FeedbackService feedbackService;
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

        //when
        when(serviceRepository.existsById(serviceId)).thenReturn(true);
        when(wishlistRepository.save(any(WishlistModel.class))).thenReturn(wishlistModel);

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
        Long userId = userModel.getId();

        WishlistModel wishlistModel = WishlistModel.builder()
                .id(1L)
                .serviceId(1L)
                .userId(userId)
                .build();

        List<WishlistModel> wishlistModels = new ArrayList<>();
        wishlistModels.add(wishlistModel);

        ServiceModel serviceModel = ServiceModel.builder()
                .id(1L)
                .providerId(2L)
                .name("Test Service")
                .type(ServiceType.valueOf("ELECTRICIAN"))
                .description("Test Description")
                .perHourRate(50.0)
                .build();

        UserModel providerModel = UserModel.builder()
                .id(2L)
                .name("Provider User")
                .image("provider_image.jpg")
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(wishlistRepository.findAllByUserId(userId)).thenReturn(wishlistModels);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(serviceModel));
        when(userRepository.findById(2L)).thenReturn(Optional.of(providerModel));
        when(feedbackService.getAverageRatingForUser(2L)).thenReturn(4.5);

        // When
        ResponseBody<List<GetWishlistResponse>> response = wishlistService.getWishlists(userId);

        // Then
        assertNotNull(response);
        verify(userRepository, times(1)).existsById(userId);
        verify(wishlistRepository, times(1)).findAllByUserId(userId);
        verify(serviceRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(feedbackService, times(1)).getAverageRatingForUser(2L);
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