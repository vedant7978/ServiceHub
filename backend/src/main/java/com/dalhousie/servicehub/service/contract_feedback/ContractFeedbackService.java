package com.dalhousie.servicehub.service.contract_feedback;

import com.dalhousie.servicehub.dto.ContractFeedbackDto;
import com.dalhousie.servicehub.response.GetContractFeedbackResponse;
import com.dalhousie.servicehub.util.ResponseBody;

public interface ContractFeedbackService {

    /**
     * Provides the contract feedback for the requesting user id
     *
     * @param contractId ID of the contract
     * @param userId     ID of the requesting user
     * @return Response body object with GetContractFeedbackResponse
     */
    ResponseBody<GetContractFeedbackResponse> getContractFeedback(long contractId, long userId);

    /**
     * Add the feedback for the provided contract
     *
     * @param contractFeedbackDto Data transfer object for contract feedback
     * @param userId              ID of the logged-in user
     * @return Response body object with String
     */
    ResponseBody<String> addContractFeedback(ContractFeedbackDto contractFeedbackDto, Long userId);
}
