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

    /**
     * Accept the contract
     *
     * @param contractId ID of the contract to accept
     * @return True if contract accepted, Otherwise false
     */
    ResponseBody<Boolean> acceptContract(Long contractId);

    /**
     * Reject the contract
     *
     * @param contractId ID of the contract to reject
     * @return True if contract rejected, Otherwise false
     */
    ResponseBody<Boolean> rejectContract(Long contractId);
}
