package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.AddServiceRequest;
import com.dalhousie.servicehub.request.UpdateServiceRequest;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.service.user_services.ManageService;
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
@RequestMapping("/api/service")
public class ServiceController {

    private static final Logger logger = LogManager.getLogger(ServiceController.class);

    @Autowired
    private ManageService manageServices;

    @PostMapping("/add-service")
    public ResponseEntity<ResponseBody<Object>> addService(
            @Valid @RequestBody AddServiceRequest addServiceRequest,
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Add service request received: {}", addServiceRequest);
            ResponseBody<Object> responseBody = manageServices.addService(addServiceRequest, userModel.getId());
            logger.info("Add service request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while adding service, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getFailureResponseBody(exception.getMessage()));
        }
    }

    @GetMapping("/get-user-services")
    public ResponseEntity<ResponseBody<GetServicesResponse>> getUserServices(@AuthenticationPrincipal UserModel userModel) {
        try {
            logger.info("Get user services request received for providerId: {}", userModel.getId());
            ResponseBody<GetServicesResponse> responseBody = manageServices.getUserServicesByProviderId(userModel.getId());
            logger.info("Get user services request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while fetching user services, {}", exception.getMessage());
            ResponseBody<GetServicesResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @DeleteMapping("/delete-service")
    public ResponseEntity<ResponseBody<Object>> deleteService(@RequestParam Long serviceId) {
        try {
            logger.info("Delete service request received for serviceId: {}", serviceId);
            ResponseBody<Object> responseBody = manageServices.deleteService(serviceId);
            logger.info("Delete service request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while deleting service, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getFailureResponseBody(exception.getMessage()));
        }
    }

    @PutMapping("/update-service")
    public ResponseEntity<ResponseBody<Object>> updateService(
            @Valid @RequestBody UpdateServiceRequest updateServiceRequest,
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Update service request received: {}", updateServiceRequest);
            ResponseBody<Object> responseBody = manageServices.updateService(updateServiceRequest, userModel.getId());
            logger.info("Update service request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while updating service, {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getFailureResponseBody(exception.getMessage()));
        }
    }

    private ResponseBody<Object> getFailureResponseBody(String message) {
        return new ResponseBody<>(FAILURE, null, message);
    }
}