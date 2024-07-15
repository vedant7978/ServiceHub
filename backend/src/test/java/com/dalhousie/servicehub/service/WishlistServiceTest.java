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
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private FeedbackService feedbackService;

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
        ServiceModel serviceModel = ServiceModel.builder()
                .id(serviceId)
                .build();

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(serviceModel));
        when(wishlistRepository.save(any(WishlistModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ResponseBody<String> response = wishlistService.addWishlist(serviceId, userModel);

        // Then
        assertEquals(ResponseBody.ResultType.SUCCESS, response.resultType());
        assertEquals("Wishlist added successfully", response.message());
        verify(wishlistRepository, times(1)).save(any(WishlistModel.class));
    }

    @Test
    @DisplayName("Add Wishlist Service Not Found")
    void addWishlist_ServiceNotFound() {
        // Given
        Long serviceId = 1L;

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

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
                .service(ServiceModel.builder().id(1L).providerId(2L).name("Test Service").type(ServiceType.Electrician).description("Test Description").perHourRate(50.0).build())
                .user(userModel)
                .build();

        List<WishlistModel> wishlistModels = new ArrayList<>();
        wishlistModels.add(wishlistModel);

        UserModel providerModel = UserModel.builder()
                .id(2L)
                .name("Provider User")
                .image("provider_image.jpg")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(wishlistRepository.findAllByUser(userModel)).thenReturn(wishlistModels);
        when(userRepository.findById(2L)).thenReturn(Optional.of(providerModel));
        when(feedbackService.getAverageRatingForUser(2L)).thenReturn(4.5);

        // When
        ResponseBody<List<GetWishlistResponse>> response = wishlistService.getWishlists(userId);

        // Then
        assertEquals(ResponseBody.ResultType.SUCCESS, response.resultType());
        assertEquals("Get wishlists successful", response.message());
        assertEquals(1, response.data().size());
        verify(userRepository, times(1)).findById(userId);
        verify(wishlistRepository, times(1)).findAllByUser(userModel);
        verify(userRepository, times(1)).findById(2L);
        verify(feedbackService, times(1)).getAverageRatingForUser(2L);
    }

    @Test
    @DisplayName("Get Wishlists User Not Found")
    void getWishlists_UserNotFound() {
        // Given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> wishlistService.getWishlists(userId));
        verify(wishlistRepository, never()).findAllByUser(any(UserModel.class));
        verify(wishlistMapper, never()).toDto(any(WishlistModel.class));
    }
}