package com.dalhousie.servicehub.service.contract_feedback;

import com.dalhousie.servicehub.dto.ContractFeedbackDto;
import com.dalhousie.servicehub.enums.FeedbackType;
import com.dalhousie.servicehub.exceptions.ContractNotFoundException;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.ContractFeedbackModel;
import com.dalhousie.servicehub.model.ContractModel;
import com.dalhousie.servicehub.model.FeedbackModel;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.repository.ContractFeedbackRepository;
import com.dalhousie.servicehub.repository.ContractRepository;
import com.dalhousie.servicehub.repository.UserRepository;
import com.dalhousie.servicehub.response.GetContractFeedbackResponse;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.SUCCESS;

@RequiredArgsConstructor
public class ContractFeedbackServiceImpl implements ContractFeedbackService {

    private final ContractFeedbackRepository repository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final FeedbackService feedbackService;

    @Override
    public ResponseBody<GetContractFeedbackResponse> getContractFeedback(long contractId, long userId) {
        if (!contractRepository.existsById(contractId))
            throw new ContractNotFoundException("Contract not found for id: " + contractId);

        ContractFeedbackModel contractFeedbackModel = getContractFeedbackModel(contractId, userId);
        GetContractFeedbackResponse response = GetContractFeedbackResponse.builder()
                .rating(contractFeedbackModel == null ? 0.0 : contractFeedbackModel.getFeedback().getRating())
                .description(contractFeedbackModel == null ? "" : contractFeedbackModel.getFeedback().getDescription())
                .build();
        return new ResponseBody<>(SUCCESS, response, "Get contract feedback successful");
    }

    @Override
    public ResponseBody<String> addContractFeedback(ContractFeedbackDto contractFeedbackDto, Long userId) {
        Long contractId = contractFeedbackDto.getContractId();
        ContractModel contractModel = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found for id: " + contractId));
        UserModel provider = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        ContractFeedbackModel contractFeedbackModel = getContractFeedbackModel(contractId, userId);
        if (contractFeedbackModel == null) {
            // Add feedback if no contract feedback available
            FeedbackModel feedbackModel = FeedbackModel.builder()
                    .provider(provider)
                    .consumer(
                            Objects.equals(contractModel.getService().getProvider().getId(), userId)
                                    ? contractModel.getUser()
                                    : contractModel.getService().getProvider()
                    )
                    .rating(contractFeedbackDto.getRating())
                    .description(contractFeedbackDto.getDescription())
                    .type(
                            Objects.equals(contractModel.getService().getProvider().getId(), userId)
                                    ? FeedbackType.ServiceProvider
                                    : FeedbackType.ServiceRequester
                    )
                    .build();
            FeedbackModel savedFeedbackModel = feedbackService.addFeedbackModel(feedbackModel);
            contractFeedbackModel = ContractFeedbackModel.builder()
                    .contract(contractModel)
                    .feedback(savedFeedbackModel)
                    .build();
        } else {
            // Update feedback if already available
            FeedbackModel feedbackModel = feedbackService.updateFeedbackModel(contractFeedbackModel.getFeedback().getId(),
                    contractFeedbackDto.getRating(),
                    contractFeedbackDto.getDescription());
            contractFeedbackModel.setFeedback(feedbackModel);
        }
        repository.save(contractFeedbackModel);
        return new ResponseBody<>(SUCCESS, "", "Add contract feedback successful");
    }

    private ContractFeedbackModel getContractFeedbackModel(long contractId, long userId) {
        return repository.findAllByContractId(contractId)
                .orElse(List.of())
                .stream()
                .filter(model -> model.getFeedback().getProvider().getId() == userId)
                .findFirst()
                .orElse(null);
    }
}
