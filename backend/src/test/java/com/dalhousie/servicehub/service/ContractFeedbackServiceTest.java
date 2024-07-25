package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.dto.ContractFeedbackDto;
import com.dalhousie.servicehub.exceptions.ContractNotFoundException;
import com.dalhousie.servicehub.model.*;
import com.dalhousie.servicehub.repository.ContractFeedbackRepository;
import com.dalhousie.servicehub.repository.ContractRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.response.GetContractFeedbackResponse;
import com.dalhousie.servicehub.service.contract_feedback.ContractFeedbackServiceImpl;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("LoggingSimilarMessage")
@ExtendWith(MockitoExtension.class)
public class ContractFeedbackServiceTest {

    private static final Logger logger = LogManager.getLogger(ContractFeedbackServiceTest.class);

    @Mock
    private ContractFeedbackRepository repository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private ContractFeedbackServiceImpl service;

    private final ContractFeedbackDto dummyContractFeedbackDto = ContractFeedbackDto.builder()
            .contractId(2L)
            .build();
    private final ContractModel dummyContractModel = ContractModel.builder()
            .id(2L)
            .service(ServiceModel.builder().provider(UserModel.builder().build()).build())
            .build();
    private final UserModel dummyUserModel = UserModel.builder()
            .id(10L)
            .build();
    private final ContractModel dummyContractModel2 = ContractModel.builder()
            .id(4L)
            .user(UserModel.builder().build())
            .service(ServiceModel.builder().provider(dummyUserModel).build())
            .build();
    private final FeedbackModel dummyFeedbackModel = FeedbackModel.builder()
            .id(3L)
            .provider(dummyUserModel)
            .rating(4.5)
            .description("description")
            .build();
    private final ContractFeedbackModel dummyContractFeedbackModel = ContractFeedbackModel.builder()
            .contract(dummyContractModel)
            .feedback(dummyFeedbackModel)
            .build();
    private final List<ContractFeedbackModel> contractFeedbackModels = List.of(
            dummyContractFeedbackModel
    );

    @Test
    public void shouldThrowException_WhenContractNotFound_AndGetContractFeedbackIsCalled() {
        // Given
        logger.info("Starting test: Throw exception when contract not found and get contract called");
        long contractId = 1;
        long userId = 10;

        // When
        ContractNotFoundException exception = assertThrows(ContractNotFoundException.class,
                () -> service.getContractFeedback(contractId, userId)
        );

        // Then
        assertEquals(exception.getMessage(), "Contract not found for id: " + contractId);
        logger.info("Test completed: Throw exception when contract not found and get contract called");
    }

    @Test
    public void shouldGetEmptyContractFeedback_WhenGetContractFeedbackIsCalled_AndNoContractFeedbackFound() {
        // Given
        logger.info("Starting test: Get empty contract feedback");
        long userId = dummyFeedbackModel.getProvider().getId();

        logger.info("Will return true when contract exists by id: {} is called", dummyContractModel.getId());
        when(contractRepository.existsById(dummyContractModel.getId())).thenReturn(true);

        logger.info("Will return empty list when findAllByContractId called for contract id: {}", dummyContractModel.getId());
        when(repository.findAllByContractId(dummyContractModel.getId())).thenReturn(Optional.of(List.of()));

        // When
        ResponseBody<GetContractFeedbackResponse> body = service.getContractFeedback(dummyContractModel.getId(), userId);

        // Then
        assertEquals(SUCCESS, body.resultType());
        assertEquals(0.0, body.data().getRating());
        assertTrue(body.data().getDescription().isEmpty());
        logger.info("Test completed: Get empty contract feedback");
    }

    @Test
    public void shouldGetContractFeedback_WhenGetContractFeedbackIsCalled() {
        // Given
        logger.info("Starting test: Get Contract Feedback");
        long userId = dummyFeedbackModel.getProvider().getId();

        logger.info("Will return true when contract exists by id: {} is called", dummyContractModel.getId());
        when(contractRepository.existsById(dummyContractModel.getId())).thenReturn(true);

        logger.info("Will return dummy contract feedback models when findAllByContractId called for contract id: {}", dummyContractModel.getId());
        when(repository.findAllByContractId(dummyContractModel.getId())).thenReturn(Optional.of(contractFeedbackModels));

        // When
        ResponseBody<GetContractFeedbackResponse> body = service.getContractFeedback(dummyContractModel.getId(), userId);

        // Then
        assertEquals(SUCCESS, body.resultType());
        assertEquals(dummyFeedbackModel.getRating(), body.data().getRating());
        assertEquals(dummyFeedbackModel.getDescription(), body.data().getDescription());
        logger.info("Test completed: Get Contract Feedback");
    }

    @Test
    public void shouldThrowException_WhenContractNotFound_AndAddContractFeedbackIsCalled() {
        // Given
        logger.info("Starting test: Throw exception when contract not found");
        long userId = 10;

        // When
        ContractNotFoundException exception = assertThrows(ContractNotFoundException.class,
                () -> service.addContractFeedback(dummyContractFeedbackDto, userId)
        );

        // Then
        assertEquals(exception.getMessage(), "Contract not found for id: " + dummyContractFeedbackDto.getContractId());
        logger.info("Test completed: Throw exception when contract not found");
    }

    @Test
    public void shouldAddContractFeedback_WhenAddContractFeedbackIsCalled() {
        // Given
        logger.info("Starting test: Add Contract Feedback");
        UserModel userModel = UserModel.builder().id(10L).build();
        ArgumentCaptor<ContractFeedbackModel> captor = ArgumentCaptor.forClass(ContractFeedbackModel.class);

        logger.info("Will return dummy contract model with id: {} when findContractById is called", dummyContractModel.getId());
        when(contractRepository.findById(dummyContractFeedbackDto.getContractId())).thenReturn(Optional.of(dummyContractModel));

        logger.info("Will return dummy feedback model with id: {} when addFeedbackModel called", dummyFeedbackModel.getId());
        when(feedbackService.addFeedbackModel(any())).thenReturn(dummyFeedbackModel);

        logger.info("Will return user with id: {} when findById called", userModel.getId());
        when(userRepository.findById(userModel.getId())).thenReturn(Optional.of(userModel));

        // When trying to add feedback for service provided by other
        ResponseBody<String> body = service.addContractFeedback(dummyContractFeedbackDto, userModel.getId());

        // Then
        verify(repository).save(captor.capture());
        assertEquals(dummyContractModel.getId(), captor.getValue().getContract().getId());
        assertEquals(dummyFeedbackModel.getId(), captor.getValue().getFeedback().getId());
        assertEquals(SUCCESS, body.resultType());

        // When trying to add feedback for service provided by user
        logger.info("Will return dummy contract model with id: {} when findContractById is called", dummyContractModel2.getId());
        when(contractRepository.findById(dummyContractFeedbackDto.getContractId())).thenReturn(Optional.of(dummyContractModel2));
        ResponseBody<String> body2 = service.addContractFeedback(dummyContractFeedbackDto, userModel.getId());

        // Then
        verify(repository, times(2)).save(captor.capture());
        assertEquals(dummyContractModel2.getId(), captor.getValue().getContract().getId());
        assertEquals(dummyFeedbackModel.getId(), captor.getValue().getFeedback().getId());
        assertEquals(SUCCESS, body2.resultType());
        logger.info("Test completed: Add Contract Feedback");
    }

    @Test
    public void shouldUpdateFeedback_WhenAddContractFeedbackIsCalled_AndFeedbackAlreadyGiven() {
        // Given
        logger.info("Starting test: Update Contract Feedback");
        UserModel userModel = UserModel.builder().id(10L).build();
        ArgumentCaptor<ContractFeedbackModel> captor = ArgumentCaptor.forClass(ContractFeedbackModel.class);

        logger.info("Will return dummy contract model with id: {} when findContractById is called", dummyContractModel.getId());
        when(contractRepository.findById(dummyContractFeedbackDto.getContractId())).thenReturn(Optional.of(dummyContractModel));

        logger.info("Will return dummy contract feedback models when findAllByContractId called for contract id: {}", dummyContractModel.getId());
        when(repository.findAllByContractId(dummyContractModel.getId())).thenReturn(Optional.of(contractFeedbackModels));

        logger.info("Will return dummy feedback model with id: {} when addFeedbackModel called", dummyFeedbackModel.getId());
        when(feedbackService.updateFeedbackModel(dummyFeedbackModel.getId(), 0.0, null)).thenReturn(dummyFeedbackModel);

        logger.info("Will return user with id: {} when findById called", userModel.getId());
        when(userRepository.findById(userModel.getId())).thenReturn(Optional.of(userModel));

        // When trying to add feedback for service provided by other
        ResponseBody<String> body = service.addContractFeedback(dummyContractFeedbackDto, userModel.getId());

        // Then
        verify(repository).save(captor.capture());
        assertEquals(dummyContractModel.getId(), captor.getValue().getContract().getId());
        assertEquals(dummyFeedbackModel.getId(), captor.getValue().getFeedback().getId());
        assertEquals(SUCCESS, body.resultType());
        logger.info("Test completed: Update Contract Feedback");
    }
}
