package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.exceptions.UserNotFoundException;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.AddFeedbackRequest;
import com.dalhousie.servicehub.response.GetFeedbackResponse;
import com.dalhousie.servicehub.service.feedback.FeedbackService;
import com.dalhousie.servicehub.util.ResponseBody;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private static final Logger logger = LogManager.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/add-feedback")
    public ResponseEntity<ResponseBody<String>> addFeedback(@Valid @RequestBody AddFeedbackRequest addFeedbackRequest) {
        try {
            logger.info("Add feedback request received: {}", addFeedbackRequest);
            ResponseBody<String> responseBody = feedbackService.addFeedback(addFeedbackRequest);
            logger.info("Add feedback request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to add feedback, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getFailureResponseBody(exception.getMessage()));
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while adding feedback, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getFailureResponseBody(exception.getMessage()));
        }
    }

    @GetMapping("/get-feedbacks")
    public ResponseEntity<ResponseBody<GetFeedbackResponse>> getFeedbacks(@AuthenticationPrincipal UserModel userModel) {
        try {
            logger.info("Get feedbacks request received for {}", userModel.getId());
            ResponseBody<GetFeedbackResponse> responseBody = feedbackService.getFeedbacks(userModel.getId());
            logger.info("Get feedbacks request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (UserNotFoundException exception) {
            logger.error("Fail to get feedback, {}", exception.getMessage());
            ResponseBody<GetFeedbackResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while getting feedback, {}", exception.getMessage());
            ResponseBody<GetFeedbackResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    private ResponseBody<String> getFailureResponseBody(String message) {
        return new ResponseBody<>(FAILURE, null, message);
    }
}
