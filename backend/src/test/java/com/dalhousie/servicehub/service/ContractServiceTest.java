package com.dalhousie.servicehub.service;

import com.dalhousie.servicehub.exceptions.ContractNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.ContractModel;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ContractRepository;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.response.GetFeedbackResponse;
import com.dalhousie.servicehub.response.GetHistoryContractsResponse;
import com.dalhousie.servicehub.response.GetPendingContractsResponse;
import com.dalhousie.servicehub.service.contract.ContractServiceImpl;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.dalhousie.servicehub.enums.ContractStatus.*;
import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("LoggingSimilarMessage")
@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    private static final Logger logger = LogManager.getLogger(ContractServiceTest.class);

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private FeedbackService feedbackService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @InjectMocks
    @Autowired
    ContractServiceImpl contractService;

    private final UserModel userModel1 = UserModel.builder().id(1L).name("One").build();
    private final UserModel userModel2 = UserModel.builder().id(2L).name("Two").build();

    private final ServiceModel serviceModel1 = ServiceModel.builder().id(3L).name("Service3").providerId(2L).build();
    private final ServiceModel serviceModel2 = ServiceModel.builder().id(4L).name("Service4").providerId(2L).build();
    private final ServiceModel serviceModel3 = ServiceModel.builder().id(5L).name("Service5").providerId(2L).build();
    private final ServiceModel serviceModel4 = ServiceModel.builder().id(6L).name("Service5").providerId(1L).build();
    private final ServiceModel serviceModel5 = ServiceModel.builder().id(7L).name("Service5").providerId(1L).build();

    private final ContractModel contractModel1 = ContractModel.builder()
            .id(3L).service(serviceModel1).user(userModel1).status(Pending).createdAt(LocalDateTime.now()).build();
    private final ContractModel contractModel2 = ContractModel.builder()
            .id(4L).service(serviceModel2).user(userModel1).status(Pending).createdAt(LocalDateTime.now()).build();
    private final ContractModel contractModel3 = ContractModel.builder()
            .id(5L).service(serviceModel3).user(userModel1).status(Accepted).createdAt(LocalDateTime.now()).build();
    private final ContractModel contractModel4 = ContractModel.builder()
            .id(8L).service(serviceModel3).user(userModel1).status(Accepted).createdAt(LocalDateTime.now()).build();
    private final ContractModel contractModel5 = ContractModel.builder()
            .id(9L).service(serviceModel4).user(userModel2).status(Pending).createdAt(LocalDateTime.now()).build();
    private final ContractModel contractModel6 = ContractModel.builder()
            .id(10L).service(serviceModel5).user(userModel2).status(Pending).createdAt(LocalDateTime.now()).build();

    @Test
    public void shouldThrowUserNotFoundException_WhenUserNotRegistered_AndGetPendingContractsIsCalled() {
        // Given
        logger.info("Starting test: Unregistered id provided to get pending contracts");
        long userId = 10;

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        logger.info("Inputting non registered user id: {}", userId);

        UserNotFoundException exception1 = assertThrows(UserNotFoundException.class,
                () -> contractService.getPendingContracts(userId)
        );

        // Then
        assertEquals(exception1.getMessage(), "User not found for id: " + userId);
        logger.info("Test completed: Unregistered id provided to get pending contracts");
    }

    @Test
    public void shouldReturnEmptyList_WhenUserHasNoServicesRegistered_AndGetPendingContractsIsCalled() {
        // Given
        logger.info("Starting test: No services registered for user and called get pending contracts");
        long userId = 10;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel1));
        when(serviceRepository.getServiceIdsByProviderId(userId)).thenReturn(List.of());

        // When
        ResponseBody<GetPendingContractsResponse> response = contractService.getPendingContracts(userId);

        // Then
        assertTrue(response.data().getContracts().isEmpty());
        logger.info("Test completed: No services registered for user and called get pending contracts");
    }

    @Test
    public void shouldProvideProperContractDetails_WhenUserHasServices_AndGetPendingContractsIsCalled() {
        // Given
        logger.info("Starting test: Get pending contracts for valid user with valid services");
        long userId = userModel2.getId();
        List<Long> servicesIds = List.of(3L, 4L, 5L);
        List<ContractModel> contracts = List.of(contractModel1, contractModel2, contractModel3, contractModel4);
        ResponseBody<GetFeedbackResponse> feedbackResponseResponseBody = new ResponseBody<>(
                SUCCESS, GetFeedbackResponse.builder().build(), ""
        );

        logger.info("Return true when existsById called for user id: {}", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel1));

        logger.info("Returned array when getServiceIdsByProviderId called for user id {}: ", servicesIds);
        when(serviceRepository.getServiceIdsByProviderId(userId)).thenReturn(servicesIds);

        logger.info("Returned array when findPendingContractsByServiceIds called for above serviceIds: {}", contracts);
        when(contractRepository.findPendingContractsByServiceIds(servicesIds)).thenReturn(contracts);

        logger.info("Returning empty feedbacks for service provider user");
        when(feedbackService.getFeedbacks(userModel1.getId())).thenReturn(feedbackResponseResponseBody);

        // When
        ResponseBody<GetPendingContractsResponse> response = contractService.getPendingContracts(userId);

        // Then
        assertEquals(response.data().getContracts().size(), contracts.size());
        logger.info("Test completed: Get pending contracts for valid user with valid services");
    }

    @Test
    public void shouldThrowUserNotFoundException_WhenUserNotRegistered_AndGetHistoryContractsIsCalled() {
        // Given
        logger.info("Starting test: Unregistered id provided to get history contracts");
        long userId = 10;

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        logger.info("Inputting non registered user id: {}", userId);

        UserNotFoundException exception1 = assertThrows(UserNotFoundException.class,
                () -> contractService.getHistoryContracts(userId)
        );

        // Then
        assertEquals(exception1.getMessage(), "User not found for id: " + userId);
        logger.info("Test completed: Unregistered id provided to get history contracts");
    }

    @Test
    public void shouldReturnEmptyList_WhenUserHasNoServicesRegistered_AndGetHistoryContractsIsCalled() {
        // Given
        logger.info("Starting test: No services completed/requested by user and called get history contracts");
        long userId = 10;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel1));
        when(serviceRepository.getServiceIdsByProviderId(userId)).thenReturn(List.of());

        // When
        ResponseBody<GetHistoryContractsResponse> response = contractService.getHistoryContracts(userId);

        // Then
        assertTrue(response.data().getContracts().isEmpty());
        logger.info("Test completed: No services completed/requested by user and called get history contracts");
    }

    @Test
    public void shouldProvideProperContractDetails_WhenUserHasServices_AndGetHistoryContractsIsCalled() {
        // Given
        logger.info("Starting test: Get history contracts for valid user with valid services");
        long userId = userModel2.getId();
        List<Long> servicesIds = List.of(3L, 4L, 5L);
        List<ContractModel> contracts = List.of(
                contractModel1,
                contractModel2,
                contractModel3,
                contractModel4,
                contractModel5,
                contractModel6
        );
        // Services Requested -> contractModel5 and contractModel6 +
        // Services offered and not pending -> contractModel3 and contractModel4
        List<ContractModel> expectedContracts = List.of(contractModel3, contractModel4, contractModel5, contractModel6);

        logger.info("Return userModel when findById called for user id: {}", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel1));

        logger.info("Returned array when getServiceIdsByProviderId called for user id {}: ", servicesIds);
        when(serviceRepository.getServiceIdsByProviderId(userId)).thenReturn(servicesIds);

        logger.info("Returned array when findHistoryContractsByServiceIds called for above serviceIds: {}", contracts);
        when(contractRepository.findHistoryContractsByServiceIds(servicesIds, userId)).thenReturn(contracts);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel2));

        // When
        ResponseBody<GetHistoryContractsResponse> response = contractService.getHistoryContracts(userId);

        // Then
        assertEquals(response.data().getContracts().size(), expectedContracts.size());
        assertEquals(response.resultType(), SUCCESS);
        logger.info("Test completed: Get history contracts for valid user with valid services");
    }

    @Test
    public void shouldThrowException_whenContractIdNotFound_AndAcceptContractIsCalled() {
        // Given
        logger.info("Starting test: Unregistered contract id provided to accept contract method");
        long contractId = 10;

        // When
        ContractNotFoundException exception1 = assertThrows(ContractNotFoundException.class,
                () -> contractService.acceptContract(contractId)
        );

        // Then
        assertEquals(exception1.getMessage(), "Contract not found for id: " + contractId);
        logger.info("Test completed: Unregistered contract id provided to accept contract method");
    }

    @Test
    public void shouldUpdateContract_whenInputIsValid_AndAcceptContractIsCalled() {
        // Given
        logger.info("Starting test: Valid input provided to accept contract method");
        long contractId = 10;
        ArgumentCaptor<ContractModel> captor = ArgumentCaptor.forClass(ContractModel.class);

        // When
        logger.info("Inputting registered contract id: {}", contractId);
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contractModel1));

        ResponseBody<Boolean> responseBody = contractService.acceptContract(contractId);

        // Then
        verify(contractRepository).save(captor.capture());
        assertEquals(SUCCESS, responseBody.resultType());
        assertTrue(responseBody.data());
        assertEquals(Accepted, captor.getValue().getStatus());
        logger.info("Test completed: Valid input provided to accept contract method");
    }

    @Test
    public void shouldThrowException_whenContractIdNotFound_AndRejectContractIsCalled() {
        // Given
        logger.info("Starting test: Unregistered contract id provided to reject contract method");
        long contractId = 10;

        // When
        ContractNotFoundException exception1 = assertThrows(ContractNotFoundException.class,
                () -> contractService.rejectContract(contractId)
        );

        // Then
        assertEquals(exception1.getMessage(), "Contract not found for id: " + contractId);
        logger.info("Test completed: Unregistered contract id provided to reject contract method");
    }

    @Test
    public void shouldUpdateContract_whenInputIsValid_AndRejectContractIsCalled() {
        // Given
        logger.info("Starting test: Valid input provided to reject contract method");
        long contractId = 10;
        ArgumentCaptor<ContractModel> captor = ArgumentCaptor.forClass(ContractModel.class);

        // When
        logger.info("Inputting registered contract id: {}", contractId);
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contractModel1));

        ResponseBody<Boolean> responseBody = contractService.rejectContract(contractId);

        // Then
        verify(contractRepository).save(captor.capture());
        assertEquals(SUCCESS, responseBody.resultType());
        assertTrue(responseBody.data());
        assertEquals(Rejected, captor.getValue().getStatus());
        logger.info("Test completed: Valid input provided to reject contract method");
    }
}
