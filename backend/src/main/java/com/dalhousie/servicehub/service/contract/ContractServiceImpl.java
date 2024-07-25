package com.dalhousie.servicehub.service.contract;

import com.dalhousie.servicehub.dto.HistoryContractDto;
import com.dalhousie.servicehub.dto.PendingContractDto;
import com.dalhousie.servicehub.enums.ContractStatus;
import com.dalhousie.servicehub.enums.HistoryType;
import com.dalhousie.servicehub.exceptions.ContractNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.ContractModel;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ContractRepository;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.response.GetHistoryContractsResponse;
import com.dalhousie.servicehub.response.GetPendingContractsResponse;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.dalhousie.servicehub.enums.ContractStatus.*;
import static com.dalhousie.servicehub.enums.HistoryType.Completed;
import static com.dalhousie.servicehub.enums.HistoryType.Requested;
import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final FeedbackService feedbackService;

    @Override
    public ResponseBody<GetPendingContractsResponse> getPendingContracts(Long userId) {
        UserModel userModel = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found for id: " + userId)
        );

        List<Long> userServicesIds = serviceRepository.getServiceIdsByProviderId(userId);
        List<ContractModel> contracts = contractRepository.findPendingContractsByServiceIds(userServicesIds);
        List<PendingContractDto> pendingContracts = contracts.stream()
                .map(this::getPendingContractDto)
                .peek((pendingContractDto) -> pendingContractDto.setServiceProviderName(userModel.getName()))
                .sorted(Comparator.comparing(PendingContractDto::getCreatedAt).reversed())
                .toList();
        GetPendingContractsResponse response = GetPendingContractsResponse.builder()
                .contracts(pendingContracts)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Get pending contracts successful");
    }

    @Override
    public ResponseBody<GetHistoryContractsResponse> getHistoryContracts(Long userId) {
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        List<Long> userServicesIds = serviceRepository.getServiceIdsByProviderId(userId);
        List<ContractModel> contracts = contractRepository.findHistoryContractsByServiceIds(userServicesIds, userId);
        List<HistoryContractDto> historyContracts = contracts.stream()
                .map(contract -> getHistoryContractDto(contract, userModel))
                .filter(historyContractDto -> {
                    if (historyContractDto.getHistoryType() == Completed)
                        return historyContractDto.getStatus() != Pending;
                    return true;
                })
                .sorted(Comparator.comparing(HistoryContractDto::getCreatedAt).reversed())
                .toList();
        GetHistoryContractsResponse response = GetHistoryContractsResponse.builder()
                .contracts(historyContracts)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Get history contracts successful");
    }

    @Override
    public ResponseBody<Boolean> acceptContract(Long contractId) {
        updateContractStatus(Accepted, contractId);
        return new ResponseBody<>(SUCCESS, true, "Contract accepted");
    }

    @Override
    public ResponseBody<Boolean> rejectContract(Long contractId) {
        updateContractStatus(Rejected, contractId);
        return new ResponseBody<>(SUCCESS, true, "Contract rejected");
    }

    private PendingContractDto getPendingContractDto(ContractModel contract) {
        ServiceModel service = contract.getService();
        UserModel user = contract.getUser();
        return PendingContractDto.builder()
                .id(contract.getId())
                .address(contract.getAddress())
                .serviceName(service.getName())
                .perHourRate(service.getPerHourRate())
                .userImageUrl(user.getImage())
                .userName(user.getName())
                .userRating(feedbackService.getAverageRatingForUser(user.getId()))
                .feedbacks(feedbackService.getFeedbacks(user.getId()).data().getFeedbacks())
                .createdAt(contract.getCreatedAt())
                .build();
    }

    private HistoryContractDto getHistoryContractDto(ContractModel contractModel, UserModel loggedInUser) {
        ServiceModel service = contractModel.getService();
        UserModel serviceProviderUser = getUser(service.getProviderId());
        UserModel user = contractModel.getUser();
        HistoryType historyType = Objects.equals(user.getId(), loggedInUser.getId()) ? Requested : Completed;
        return HistoryContractDto.builder()
                .id(contractModel.getId())
                .serviceName(service.getName())
                .serviceType(service.getType())
                .historyType(historyType)
                .serviceProviderId(historyType == Completed ? user.getId() : serviceProviderUser.getId())
                .serviceProviderName(serviceProviderUser.getName())
                .serviceRequesterName(user.getName())
                .perHourRate(service.getPerHourRate())
                .serviceDescription(service.getDescription())
                .userImageUrl(historyType == Completed ? user.getImage() : serviceProviderUser.getImage())
                .userName(historyType == Completed ? user.getName() : serviceProviderUser.getName())
                .status(contractModel.getStatus())
                .createdAt(contractModel.getCreatedAt())
                .build();
    }

    private UserModel getUser(long userId) {
        return userRepository.findById(userId).orElse(UserModel.builder().build());
    }

    private void updateContractStatus(ContractStatus status, long contractId) {
        ContractModel contractModel = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found for id: " + contractId));
        contractModel.setStatus(status);
        contractRepository.save(contractModel);
    }
}
