package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.dto.FeedbackDto;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.mapper.FeedbackMapper;
import com.dalhousie.servicehub.model.FeedbackModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.FeedbackRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.request.AddFeedbackRequest;
import com.dalhousie.servicehub.response.GetFeedbackResponse;
import com.dalhousie.servicehub.service.feedback.FeedbackServiceImpl;
import com.dalhousie.servicehub.util.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    private static final Logger logger = LogManager.getLogger(FeedbackServiceTest.class);

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FeedbackMapper feedbackMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @InjectMocks
    @Autowired
    FeedbackServiceImpl feedbackService;

    private final FeedbackDto dummyFeedbackDto = FeedbackDto.builder()
            .rating(3.5).description("DummyDescription").build();
    private final List<FeedbackDto> dummyFeedbackDtoList = List.of(
            dummyFeedbackDto,
            dummyFeedbackDto,
            dummyFeedbackDto,
            dummyFeedbackDto,
            dummyFeedbackDto
    );
    private final List<FeedbackModel> dummyFeedbacks = List.of(
            FeedbackModel.builder().id(1L).rating(1.0).description("Description1").build(),
            FeedbackModel.builder().id(2L).rating(2.0).description("Description2").build(),
            FeedbackModel.builder().id(3L).rating(3.0).description("Description3").build(),
            FeedbackModel.builder().id(4L).rating(4.0).description("Description4").build(),
            FeedbackModel.builder().id(5L).rating(5.0).description("Description5").build()
    );

    @Test
    public void shouldThrowNPE_WhenInputIsInvalid_AndAddFeedbackIsCalled() {
        // Given
        logger.info("Starting test: Invalid input provided to add feedback");
        AddFeedbackRequest invalidRequest = new AddFeedbackRequest();

        // When & Then
        logger.info("Input: {}", invalidRequest);
        assertThrows(NullPointerException.class, () -> feedbackService.addFeedback(invalidRequest));
        logger.info("Test completed: Invalid input provided to add feedback");
    }

    @Test
    public void shouldThrowUserNotFoundException_WhenUserNotRegistered_AndAddFeedbackIsCalled() {
        // Given
        long providerId = 10;
        long consumerId = 12;
        logger.info("Starting test: Unregistered id provided to add feedback");
        AddFeedbackRequest addFeedbackRequest = AddFeedbackRequest.builder()
                .providerId(providerId)
                .consumerId(consumerId)
                .build();

        // When
        when(userRepository.existsById(providerId)).thenReturn(false);
        when(userRepository.existsById(consumerId)).thenReturn(true);
        logger.info("Inputting non registered providerId: {}", addFeedbackRequest);

        UserNotFoundException exception1 = assertThrows(UserNotFoundException.class,
                () -> feedbackService.addFeedback(addFeedbackRequest)
        );

        // Then
        assertEquals(exception1.getMessage(), "User not found for id: " + providerId);

        // When
        when(userRepository.existsById(providerId)).thenReturn(true);
        when(userRepository.existsById(consumerId)).thenReturn(false);
        logger.info("Inputting non registered consumerId: {}", addFeedbackRequest);

        UserNotFoundException exception2 = assertThrows(UserNotFoundException.class,
                () -> feedbackService.addFeedback(addFeedbackRequest)
        );

        // Then
        assertEquals(exception2.getMessage(), "User not found for id: " + consumerId);
        logger.info("Test completed: Unregistered id provided to add feedback");
    }

    @Test
    public void shouldAddFeedback_WhenInputIsValid_AndAddFeedbackIsCalled() {
        // Given
        logger.info("Starting test: Valid input provided to add feedback");
        long providerId = 10;
        long consumerId = 12;
        double rating = 5.0;
        String description = "Person works well";
        when(userRepository.existsById(providerId)).thenReturn(true);
        when(userRepository.existsById(consumerId)).thenReturn(true);
        AddFeedbackRequest addFeedbackRequest = AddFeedbackRequest.builder()
                .providerId(providerId)
                .consumerId(consumerId)
                .rating(rating)
                .description(description)
                .build();

        // When
        logger.info("Providing valid input to add feedback: {}", addFeedbackRequest);
        ResponseBody<String> responseBody = feedbackService.addFeedback(addFeedbackRequest);

        // Then
        logger.info("Response body after providing valid input: {}", responseBody);
        verify(feedbackRepository).save(any());
        assertEquals(responseBody.resultType(), SUCCESS);
        assertEquals(responseBody.message(), "Add feedback successful");
        logger.info("Test completed: Valid input provided to add feedback");
    }

    @Test
    public void shouldThrowUserNotFoundException_WhenUserIsNotRegistered_AndGetFeedbacksIsCalled() {
        // Given
        logger.info("Starting test: Unregistered user id to get feedbacks method");
        long userId = 24;

        // When
        when(userRepository.existsById(userId)).thenReturn(false);
        logger.info("Passing non registered user id to getFeedbacks: {}", userId);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> feedbackService.getFeedbacks(userId)
        );

        // Then
        assertEquals(exception.getMessage(), "User not found for id: " + userId);
        logger.info("Test completed: Unregistered user id to get feedbacks method");
    }

    @Test
    public void shouldProvideFeedbacks_WhenUserIsRegistered_AndGetFeedbacksIsCalled_ButListIsEmpty() {
        // Given
        logger.info("Starting test: Registered user id to get feedbacks method with no feedbacks in database");
        long userId = 3;
        when(userRepository.existsById(userId)).thenReturn(true);
        when(feedbackRepository.findAllByConsumerId(userId)).thenReturn(Optional.empty());

        // When
        logger.info("Passing registered user id (having 0 feedbacks) to getFeedbacks: {}", userId);
        ResponseBody<GetFeedbackResponse> responseBody = feedbackService.getFeedbacks(userId);

        // Then
        logger.info("Response body after getting feedbacks: {}", responseBody);
        verify(feedbackRepository).findAllByConsumerId(userId);
        assertEquals(responseBody.resultType(), SUCCESS);
        assertTrue(responseBody.data().getFeedbacks().isEmpty());
        assertEquals(responseBody.message(), "Get feedbacks successful");
        logger.info("Test completed: Registered user id to get feedbacks method with no feedbacks in database");
    }

    @Test
    public void shouldProvideFeedbacks_WhenUserIsRegistered_AndGetFeedbacksIsCalled() {
        // Given
        logger.info("Starting test: Registered user id to get feedbacks method with some feedbacks in database");
        long userId = 3;
        long providerId = 4;
        String providerName = "DummyName";
        UserModel providerUserModel = UserModel.builder().id(providerId).name(providerName).build();
        when(userRepository.existsById(userId)).thenReturn(true);
        when(feedbackRepository.findAllByConsumerId(userId)).thenReturn(Optional.of(dummyFeedbacks));

        // When
        logger.info("Passing registered user id (having some feedbacks) to getFeedbacks: {}", userId);
        logger.info("Provider userId is NOT registered into database: {}", providerUserModel);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(feedbackMapper.toDto(any())).thenReturn(dummyFeedbackDto);
        ResponseBody<GetFeedbackResponse> responseBody1 = feedbackService.getFeedbacks(userId);

        // Then
        logger.info("Response body after getting feedbacks and Provider userId is NOT registered: {}", responseBody1);
        verify(feedbackRepository).findAllByConsumerId(userId);
        assertEquals(responseBody1.resultType(), SUCCESS);
        assertEquals(responseBody1.data().getFeedbacks(), dummyFeedbackDtoList);
        assertEquals(responseBody1.message(), "Get feedbacks successful");

        // When
        logger.info("Passing registered user id (having some feedbacks) to getFeedbacks: {}", userId);
        logger.info("Provider userId is registered into database: {}", providerUserModel);
        when(userRepository.findById(any())).thenReturn(Optional.of(providerUserModel));
        ResponseBody<GetFeedbackResponse> responseBody2 = feedbackService.getFeedbacks(userId);

        // Then
        List<FeedbackDto> expectedResult = dummyFeedbacks.stream()
                .map(model -> FeedbackDto.builder()
                        .providerName(providerName)
                        .rating(model.getRating())
                        .description(model.getDescription())
                        .build())
                .toList();
        logger.info("Response body after getting feedbacks and Provider userId is registered: {}", responseBody2);
        verify(feedbackRepository, times(2)).findAllByConsumerId(userId);
        assertEquals(responseBody2.resultType(), SUCCESS);
        assertEquals(responseBody2.data().getFeedbacks(), expectedResult);
        assertEquals(responseBody2.message(), "Get feedbacks successful");
        logger.info("Test completed: Registered user id to get feedbacks method with some feedbacks in database");
    }

    @Test
    public void shouldReturnZeroRating_WhenNoFeedbackPresentForUser_AndGetAverageRatingForUserCalled() {
        // Given
        logger.info("Test start: No feedback found for the requesting user");
        long userId = 10;
        when(feedbackRepository.findAllByConsumerId(userId)).thenReturn(Optional.empty());

        // When
        Double averageRatingForUser = feedbackService.getAverageRatingForUser(userId);

        // Then
        assertEquals(averageRatingForUser, 0.0);
        logger.info("Test completed: No feedback found for the requesting user");
    }

    @Test
    public void shouldReturnProperRating_WhenFeedbacksArePresentForUser_AndGetAverageRatingForUserCalled() {
        // Given
        logger.info("Test start: Get average rating from some feedbacks already present for the requesting user");
        long userId = 10;
        when(feedbackRepository.findAllByConsumerId(userId)).thenReturn(Optional.of(dummyFeedbacks));

        // When
        Double averageRatingForUser = feedbackService.getAverageRatingForUser(userId);

        // Then
        assertEquals(averageRatingForUser, 3.0); // Manually averaging value
        logger.info("Test completed: Get average rating from some feedbacks already present for the requesting user");
    }
}
