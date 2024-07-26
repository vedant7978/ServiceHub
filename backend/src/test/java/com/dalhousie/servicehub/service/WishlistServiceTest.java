package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.exceptions.ServiceNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.exceptions.WishlistNotFoundException;
import com.dalhousie.servicehub.mapper.ServiceMapper;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.model.WishlistModel;
import com.dalhousie.servicehub.repository.ContractRepository;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.repository.WishlistRepository;
import com.dalhousie.servicehub.response.GetFeedbackResponse;
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

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private ContractRepository contractRepository;

    @Mock
    private ServiceMapper serviceMapper;

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
        UserModel providerModel = UserModel.builder()
                .id(2L)
                .name("Provider User")
                .image("provider_image.jpg")
                .build();
        ServiceModel serviceModel = ServiceModel.builder()
                .id(1L)
                .provider(providerModel)
                .name("Test Service")
                .type(ServiceType.Electrician)
                .description("Test Description")
                .perHourRate(50.0)
                .build();
        ServiceDto serviceDto = ServiceDto.builder().providerId(providerModel.getId()).build();
        WishlistModel wishlistModel = WishlistModel.builder()
                .id(1L)
                .service(serviceModel)
                .user(userModel)
                .build();

        List<WishlistModel> wishlistModels = new ArrayList<>();
        wishlistModels.add(wishlistModel);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(wishlistRepository.findAllByUser(userModel)).thenReturn(wishlistModels);
        when(feedbackService.getAverageRatingForUser(providerModel.getId())).thenReturn(4.5);
        when(serviceMapper.toDto(serviceModel)).thenReturn(serviceDto);
        when(contractRepository.existsByServiceIdAndUserId(anyLong(), anyLong())).thenReturn(false);
        ResponseBody<GetFeedbackResponse> feedbackResponseResponseBody = new ResponseBody<>(
                SUCCESS, GetFeedbackResponse.builder().build(), ""
        );
        when(feedbackService.getFeedbacks(anyLong())).thenReturn(feedbackResponseResponseBody);

        // When
        ResponseBody<GetWishlistResponse> response = wishlistService.getWishlists(userId);

        // Then
        assertEquals(ResponseBody.ResultType.SUCCESS, response.resultType());
        assertEquals("Get wishlists successful", response.message());
        assertEquals(1, response.data().getServices().size());
        verify(wishlistRepository, times(1)).findAllByUser(userModel);
        verify(feedbackService, times(1)).getAverageRatingForUser(providerModel.getId());
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
    }

    @Test
    @DisplayName("Delete Wishlist Successfully")
    void deleteWishlist_Success() {
        // Given
        Long wishlistId = 1L;
        WishlistModel wishlistModel = WishlistModel.builder()
                .id(wishlistId)
                .service(ServiceModel.builder().id(1L).build())
                .user(userModel)
                .build();

        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlistModel));

        // When
        ResponseBody<String> response = wishlistService.deleteWishlist(wishlistId);

        // Then
        assertEquals(ResponseBody.ResultType.SUCCESS, response.resultType());
        assertEquals("Wishlist deleted successfully", response.message());
        verify(wishlistRepository, times(1)).findById(wishlistId);
        verify(wishlistRepository, times(1)).delete(wishlistModel);
    }

    @Test
    @DisplayName("Delete Wishlist Not Found")
    void deleteWishlist_WishlistNotFound() {
        // Given
        Long wishlistId = 1L;

        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(WishlistNotFoundException.class, () -> wishlistService.deleteWishlist(wishlistId));
        verify(wishlistRepository, times(1)).findById(wishlistId);
        verify(wishlistRepository, never()).delete(any(WishlistModel.class));
    }
}