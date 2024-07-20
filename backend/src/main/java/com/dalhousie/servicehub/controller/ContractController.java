package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.response.AcceptRejectContractRequest;
import com.dalhousie.servicehub.response.GetHistoryContractsResponse;
import com.dalhousie.servicehub.response.GetPendingContractsResponse;
import com.dalhousie.servicehub.service.contract.ContractService;
import com.dalhousie.servicehub.util.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contract")
public class ContractController {

    private static final Logger logger = LogManager.getLogger(ContractController.class);
    private final ContractService contractService;

    @GetMapping("/get-pending-contracts")
    public ResponseEntity<ResponseBody<GetPendingContractsResponse>> getPendingContracts(
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Get pending contracts request received for {}", userModel.getId());
            ResponseBody<GetPendingContractsResponse> responseBody = contractService.getPendingContracts(userModel.getId());
            logger.info("Get pending contracts request success");
            return ResponseEntity.ok(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to get pending contracts, {}", exception.getMessage());
            ResponseBody<GetPendingContractsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while getting pending contracts, {}", exception.getMessage());
            ResponseBody<GetPendingContractsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @GetMapping("/get-history-contracts")
    public ResponseEntity<ResponseBody<GetHistoryContractsResponse>> getHistoryContracts(
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Get history contracts request received for {}", userModel.getId());
            ResponseBody<GetHistoryContractsResponse> responseBody = contractService.getHistoryContracts(userModel.getId());
            logger.info("Get history contracts request success");
            return ResponseEntity.ok(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to get history contracts, {}", exception.getMessage());
            ResponseBody<GetHistoryContractsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while getting history contracts, {}", exception.getMessage());
            ResponseBody<GetHistoryContractsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PostMapping("/accept-contract")
    public ResponseEntity<ResponseBody<Boolean>> acceptContract(@RequestBody AcceptRejectContractRequest request) {
        try {
            logger.info("Accept contract request received for {}", request.getContractId());
            ResponseBody<Boolean> responseBody = contractService.acceptContract(request.getContractId());
            logger.info("Accept contract success");
            return ResponseEntity.ok(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to accept contract, {}", exception.getMessage());
            ResponseBody<Boolean> body = new ResponseBody<>(FAILURE, false, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while accepting contract, {}", exception.getMessage());
            ResponseBody<Boolean> body = new ResponseBody<>(FAILURE, false, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PostMapping("/reject-contract")
    public ResponseEntity<ResponseBody<Boolean>> rejectContract(@RequestBody AcceptRejectContractRequest request) {
        try {
            logger.info("Reject contract request received for {}", request.getContractId());
            ResponseBody<Boolean> responseBody = contractService.rejectContract(request.getContractId());
            logger.info("Reject contract success");
            return ResponseEntity.ok(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to reject contract, {}", exception.getMessage());
            ResponseBody<Boolean> body = new ResponseBody<>(FAILURE, false, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while rejecting contract, {}", exception.getMessage());
            ResponseBody<Boolean> body = new ResponseBody<>(FAILURE, false, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }
}
