package com.dalhousie.servicehub.service.contract;

import com.dalhousie.servicehub.response.GetHistoryContractsResponse;
import com.dalhousie.servicehub.response.GetPendingContractsResponse;
import com.dalhousie.servicehub.util.ResponseBody;

public interface ContractService {
    /**
     * Provide all the contracts that are pending to be accepted or rejected
     *
     * @param userId ID of the user to get pending contracts
     * @return List of all contract details
     */
    ResponseBody<GetPendingContractsResponse> getPendingContracts(Long userId);

    /**
     * Provide all the history of contracts
     *
     * @param userId ID of the user to get contracts history
     * @return List of all contract details
     */
    ResponseBody<GetHistoryContractsResponse> getHistoryContracts(Long userId);
}
