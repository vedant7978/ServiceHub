package com.dalhousie.servicehub.service.contract;

import com.dalhousie.servicehub.dto.PendingContractDto;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.ContractModel;
import com.dalhousie.servicehub.model.ServiceModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ContractRepository;
import com.dalhousie.servicehub.repository.ServiceRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.response.GetPendingContractsResponse;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .map(contract -> {
                    ServiceModel service = contract.getService();
                    UserModel user = contract.getUser();
                    return PendingContractDto.builder()
                            .id(contract.getId())
                            .address(contract.getAddress())
                            .serviceName(service.getName())
                            .userImageUrl(user.getImage())
                            .userName(user.getName())
                            .userRating(feedbackService.getAverageRatingForUser(userId))
                            .feedbacks(feedbackService.getFeedbacks(userId).data().getFeedbacks())
                            .build();
                })
                .toList();
        GetPendingContractsResponse response = GetPendingContractsResponse.builder()
                .contracts(pendingContracts)
                .build();
        return new ResponseBody<>(SUCCESS, response, "Get pending contracts successful");
    }
}
