package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.dto.ContractFeedbackDto;
import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.response.GetContractFeedbackResponse;
import com.dalhousie.servicehub.service.contract_feedback.ContractFeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/contract-feedback")
public class ContractFeedbackController {

    private static final Logger logger = LogManager.getLogger(ContractFeedbackController.class);

    @Autowired
    private ContractFeedbackService contractFeedbackService;

    @GetMapping("/get-contract-feedback")
    public ResponseEntity<ResponseBody<GetContractFeedbackResponse>> getContractFeedback(
            @AuthenticationPrincipal UserModel userModel,
            @RequestParam long contractId
    ) {
        try {
            logger.info("Get contract feedback request received for {}", contractId);
            ResponseBody<GetContractFeedbackResponse> responseBody = contractFeedbackService.getContractFeedback(contractId, userModel.getId());
            logger.info("Get contract feedback success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to get contract feedback, {}", exception.getMessage());
            ResponseBody<GetContractFeedbackResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while getting contract feedback, {}", exception.getMessage());
            ResponseBody<GetContractFeedbackResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @PostMapping("/add-contract-feedback")
    public ResponseEntity<ResponseBody<String>> addContractFeedback(
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody ContractFeedbackDto contractFeedbackDto
    ) {
        try {
            logger.info("Add contract feedback request received for {}", contractFeedbackDto.getContractId());
            ResponseBody<String> responseBody = contractFeedbackService.addContractFeedback(contractFeedbackDto, userModel.getId());
            logger.info("Add contract feedback success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to add contract feedback, {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, "", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while adding contract feedback, {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, "", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }
}
