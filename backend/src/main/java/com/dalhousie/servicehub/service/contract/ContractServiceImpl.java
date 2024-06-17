package com.dalhousie.servicehub.service.contract;

import com.dalhousie.servicehub.dto.HistoryContractDto;
import com.dalhousie.servicehub.dto.PendingContractDto;
import com.dalhousie.servicehub.enums.HistoryType;
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
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.dalhousie.servicehub.enums.HistoryType.Completed;
import static com.dalhousie.servicehub.enums.HistoryType.Requested;
import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final FeedbackService feedbackService;

    @Override
    public ResponseBody<GetPendingContractsResponse> getPendingContracts(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException("User not found for id: " + userId);

        List<Long> userServicesIds = serviceRepository.getServiceIdsByProviderId(userId);
        List<ContractModel> contracts = contractRepository.findPendingContractsByServiceIds(userServicesIds);
        List<PendingContractDto> pendingContracts = contracts.stream()
                .map(this::getPendingContractDto)
                .sorted(Comparator.comparing(PendingContractDto::getCreatedAt).reversed())
                .toList();
        GetPendingContractsResponse response = GetPendingContractsResponse.builder()
                .contracts(pendingContracts)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Get pending contracts successful");
    }

    @Override
    public ResponseBody<GetHistoryContractsResponse> getHistoryContracts(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException("User not found for id: " + userId);

        List<Long> userServicesIds = serviceRepository.getServiceIdsByProviderId(userId);
        List<ContractModel> contracts = contractRepository.findHistoryContractsByServiceIds(userServicesIds, userId);
        List<HistoryContractDto> historyContracts = contracts.stream()
                .map(contract -> getHistoryContractDto(contract, userId))
                .sorted(Comparator.comparing(HistoryContractDto::getCreatedAt).reversed())
                .toList();
        GetHistoryContractsResponse response = GetHistoryContractsResponse.builder()
                .contracts(historyContracts)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Get history contracts successful");
    }

    private PendingContractDto getPendingContractDto(ContractModel contract) {
        ServiceModel service = contract.getService();
        UserModel user = contract.getUser();
        return PendingContractDto.builder()
                .id(contract.getId())
                .address(contract.getAddress())
                .serviceName(service.getName())
                .userImageUrl(user.getImage())
                .userName(user.getName())
                .userRating(feedbackService.getAverageRatingForUser(user.getId()))
                .feedbacks(feedbackService.getFeedbacks(user.getId()).data().getFeedbacks())
                .createdAt(contract.getCreatedAt())
                .build();
    }

    private HistoryContractDto getHistoryContractDto(ContractModel contractModel, long loggedInUserId) {
        ServiceModel service = contractModel.getService();
        UserModel serviceProviderUser = getUser(service.getProviderId());
        UserModel user = contractModel.getUser();
        HistoryType historyType = Objects.equals(user.getId(), loggedInUserId) ? Requested : Completed;
        return HistoryContractDto.builder()
                .id(contractModel.getId())
                .serviceName(service.getName())
                .serviceType(service.getType())
                .historyType(historyType)
                .serviceDescription(service.getDescription())
                .userImageUrl(historyType == Completed ? user.getImage() : serviceProviderUser.getImage())
                .userName(historyType == Completed ? user.getName() : serviceProviderUser.getName())
                .isPending(contractModel.isPending())
                .createdAt(contractModel.getCreatedAt())
                .build();
    }

    private UserModel getUser(long userId) {
        return userRepository.findById(userId).orElse(UserModel.builder().build());
    }
}
