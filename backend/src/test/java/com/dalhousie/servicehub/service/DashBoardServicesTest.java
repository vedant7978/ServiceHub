package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.dto.ServiceDto;
import com.dalhousie.servicehub.dto.UserDto;
import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.mapper.ServiceMapper;
import com.dalhousie.servicehub.mapper.UserMapper;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.repository.WishlistRepository;
import com.dalhousie.servicehub.response.GetProviderResponse;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.service.dashboard_services.DashboardServicesImpl;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import com.dalhousie.servicehub.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashBoardServicesTest {
    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceMapper serviceMapper;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private DashboardServicesImpl dashboardServices;

    @BeforeEach
    public void setUp() {
        mockSecurityContextWithAuthenticatedUser();
    }

    @Test
    public void shouldGetAllServices_WhenServicesExist() {
        // Mock the static method SecurityUtils.getLoggedInUserId
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            // Given
            Long loggedInUserId = 1L;
            mockedSecurityUtils.when(SecurityUtils::getLoggedInUserId).thenReturn(loggedInUserId);

            List<ServiceModel> serviceModels = Arrays.asList(
                    ServiceModel.builder().id(1L).name("Service 1").description("Description 1").perHourRate(50.0).type(ServiceType.Plumbing).providerId(2L).build(),
                    ServiceModel.builder().id(2L).name("Service 2").description("Description 2").perHourRate(60.0).type(ServiceType.Electrician).providerId(2L).build()
            );
            List<ServiceDto> serviceDtos = Arrays.asList(
                    new ServiceDto(1L, "Description 1", "Service 1", 50.0, ServiceType.Plumbing, 2L, false, 4.5),
                    new ServiceDto(2L, "Description 2", "Service 2", 60.0, ServiceType.Electrician, 2L, false, 4.0)
            );

            // Mock the behavior of serviceRepository.findAll()
            when(serviceRepository.findAll()).thenReturn(serviceModels);

            // Mock the behavior of serviceMapper.toDto()
            when(serviceMapper.toDto(any(ServiceModel.class))).thenAnswer(
                    invocation -> {
                        ServiceModel model = invocation.getArgument(0);
                        return new ServiceDto(model.getId(), model.getDescription(), model.getName(), model.getPerHourRate(), model.getType(), model.getProviderId(), false, null);
                    }
            );

            // Mock the behavior of serviceRepository.findById()
            when(serviceRepository.findById(any(Long.class))).thenAnswer(
                    invocation -> {
                        Long id = invocation.getArgument(0);
                        return serviceModels.stream()
                                .filter(serviceModel -> serviceModel.getId().equals(id))
                                .findFirst();
                    }
            );

            // Mock the behavior of wishlistRepository.existsByServiceAndUser()
            when(wishlistRepository.existsByServiceAndUser(any(ServiceModel.class), any(UserModel.class))).thenReturn(false);

            // Mock the behavior of userRepository.findById()
            when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new UserModel()));
            when(feedbackService.getAverageRatingForUser(anyLong())).thenReturn(4.5);

            // When
            ResponseBody<GetServicesResponse> responseBody = dashboardServices.getAllServices();

            // Then
            assertEquals(SUCCESS, responseBody.resultType());
            assertEquals(serviceDtos.size(), responseBody.data().getServices().size());
            verify(serviceRepository, times(1)).findAll();
            verify(serviceMapper, times(serviceModels.size())).toDto(any(ServiceModel.class));
        }
    }

    @Test
    public void shouldGetServicesByType_WhenServicesExist_ForGivenType() {
        // Given
        ServiceType type = ServiceType.Plumbing;
        List<ServiceModel> serviceModels = Arrays.asList(
                ServiceModel.builder().id(1L).name("Service 1").description("Description 1").perHourRate(50.0).type(type).providerId(2L).build(),
                ServiceModel.builder().id(2L).name("Service 2").description("Description 2").perHourRate(60.0).type(type).providerId(2L).build()
        );
        List<ServiceDto> serviceDtos = Arrays.asList(
                new ServiceDto(1L, "Description 1", "Service 1", 50.0, type, 2L, false, 4.5),
                new ServiceDto(2L, "Description 2", "Service 2", 60.0, type, 2L, false, 4.0)
        );

        // When
        when(serviceRepository.findByType(type)).thenReturn(serviceModels);
        when(serviceMapper.toDto(any(ServiceModel.class))).thenAnswer(
                invocation -> {
                    ServiceModel model = invocation.getArgument(0);
                    return new ServiceDto(model.getId(), model.getDescription(), model.getName(), model.getPerHourRate(), model.getType(), model.getProviderId(), false, null);
                }
        );

        // Mock the behavior of serviceRepository.findById()
        when(serviceRepository.findById(any(Long.class))).thenAnswer(
                invocation -> {
                    Long id = invocation.getArgument(0);
                    return serviceModels.stream()
                            .filter(serviceModel -> serviceModel.getId().equals(id))
                            .findFirst();
                }
        );


        // Mock the behavior of userRepository.findById()
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new UserModel()));
        when(feedbackService.getAverageRatingForUser(anyLong())).thenReturn(4.5);

        ResponseBody<GetServicesResponse> responseBody = dashboardServices.getServicesByType(type);

        // Then
        assertEquals(SUCCESS, responseBody.resultType());
        assertEquals(serviceDtos.size(), responseBody.data().getServices().size());
        verify(serviceRepository, times(1)).findByType(type);
        verify(serviceMapper, times(serviceModels.size())).toDto(any(ServiceModel.class));
    }


    @Test
    public void shouldSearchServicesByName_WhenServicesExist_ForGivenName() {
        // Given
        String name = "Service";
        List<ServiceModel> serviceModels = Arrays.asList(
                ServiceModel.builder().id(1L).name("Service 1").description("Description 1").perHourRate(50.0).type(ServiceType.Plumbing).providerId(2L).build(),
                ServiceModel.builder().id(2L).name("Service 2").description("Description 2").perHourRate(60.0).type(ServiceType.Electrician).providerId(2L).build()
        );
        List<ServiceDto> serviceDtos = Arrays.asList(
                new ServiceDto(1L, "Description 1", "Service 1", 50.0, ServiceType.Plumbing, 2L, false, 4.5),
                new ServiceDto(2L, "Description 2", "Service 2", 60.0, ServiceType.Electrician, 2L, false, 4.0)
        );

        // When
        when(serviceRepository.findByNameContainingIgnoreCase(name)).thenReturn(serviceModels);
        when(serviceMapper.toDto(any(ServiceModel.class))).thenAnswer(
                invocation -> {
                    ServiceModel model = invocation.getArgument(0);
                    return new ServiceDto(model.getId(), model.getDescription(), model.getName(), model.getPerHourRate(), model.getType(), model.getProviderId(), false, null);
                }
        );
        // Mock the behavior of serviceRepository.findById()
        when(serviceRepository.findById(any(Long.class))).thenAnswer(
                invocation -> {
                    Long id = invocation.getArgument(0);
                    return serviceModels.stream()
                            .filter(serviceModel -> serviceModel.getId().equals(id))
                            .findFirst();
                }
        );

        // Mock the behavior of userRepository.findById()
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new UserModel()));
        when(feedbackService.getAverageRatingForUser(anyLong())).thenReturn(4.5);

        ResponseBody<GetServicesResponse> responseBody = dashboardServices.searchServicesByName(name);

        // Then
        assertEquals(SUCCESS, responseBody.resultType());
        assertEquals(serviceDtos.size(), responseBody.data().getServices().size());
        verify(serviceRepository, times(1)).findByNameContainingIgnoreCase(name);
        verify(serviceMapper, times(serviceModels.size())).toDto(any(ServiceModel.class));
    }

    @Test
    public void shouldGetProviderDetailsById_WhenProviderExists() {
        // Given
        Long providerId = 1L;
        UserModel userModel = UserModel.builder().id(providerId).name("vedant patel").email("vedant@example.com").build();
        UserDto userDto = UserDto.builder().id(providerId).name("vedant patel").email("vedant@example.com").build();

        // When
        when(userRepository.findById(providerId)).thenReturn(Optional.of(userModel));
        when(userMapper.toDto(userModel)).thenReturn(userDto);

        ResponseBody<GetProviderResponse> responseBody = dashboardServices.getProviderDetailsById(providerId);

        // Then
        assertEquals(SUCCESS, responseBody.resultType());
        assertEquals(userDto, responseBody.data().getProvider());
        verify(userRepository, times(1)).findById(providerId);
        verify(userMapper, times(1)).toDto(userModel);
    }

    @Test
    public void shouldReturnFailure_WhenProviderDoesNotExist() {
        // Given
        Long providerId = 1L;
        when(userRepository.findById(providerId)).thenReturn(Optional.empty());

        // When
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> dashboardServices.getProviderDetailsById(providerId));

        // Then
        assertEquals("User not found with ID: " + providerId, userNotFoundException.getMessage());
        verify(userRepository, times(1)).findById(providerId);
        verify(userMapper, never()).toDto(any(UserModel.class));
    }

    @Test
    public void shouldThrowException_WhenUserNotAuthenticated() {
        // Given
        mockSecurityContextWithUnauthenticatedUser();

        // When / Then
        assertThrows(IllegalStateException.class, SecurityUtils::getLoggedInUserId);
    }

    @Test
    public void shouldThrowException_WhenAuthenticationIsNull() {
        // Given
        mockSecurityContextWithNullAuthentication();

        // When / Then
        assertThrows(IllegalStateException.class, SecurityUtils::getLoggedInUserId);
    }

    private void mockSecurityContextWithAuthenticatedUser() {
        UserModel userModel = UserModel.builder().id(1L).name("Test User").build();
        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.isAuthenticated()).thenReturn(true);
        lenient().when(authentication.getPrincipal()).thenReturn(userModel);

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    private void mockSecurityContextWithUnauthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.isAuthenticated()).thenReturn(false);

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    private void mockSecurityContextWithNullAuthentication() {
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(null);

        SecurityContextHolder.setContext(securityContext);
    }
}
