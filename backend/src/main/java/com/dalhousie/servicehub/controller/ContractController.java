package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.response.GetPendingContractsResponse;
import com.dalhousie.servicehub.service.contract.ContractService;
import com.dalhousie.servicehub.util.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/contract")
public class ContractController {

    private static final Logger logger = LogManager.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;

    @GetMapping("/get-pending-contracts")
    public ResponseEntity<ResponseBody<GetPendingContractsResponse>> getPendingContracts(
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Get pending contracts request received for {}", userModel.getId());
            ResponseBody<GetPendingContractsResponse> responseBody = contractService.getPendingContracts(userModel.getId());
            logger.info("Get pending contracts request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to get pending contracts, {}", exception.getMessage());
            ResponseBody<GetPendingContractsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while getting pending contracts, {}", exception.getMessage());
            ResponseBody<GetPendingContractsResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }
}
