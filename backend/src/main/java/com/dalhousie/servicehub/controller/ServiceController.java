package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.factory.service.ServiceFactory;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.AddServiceRequest;
import com.dalhousie.servicehub.request.UpdateServiceRequest;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.service.user_services.ManageService;
import com.dalhousie.servicehub.util.ResponseBody;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    private static final Logger logger = LogManager.getLogger(ServiceController.class);
    private final ManageService manageServices;

    public ServiceController(ServiceFactory serviceFactory) {
        manageServices = serviceFactory.getManageService();
    }

    @PostMapping("/add-service")
    public ResponseEntity<ResponseBody<String>> addService(
            @Valid @RequestBody AddServiceRequest addServiceRequest,
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Add service request received: {}", addServiceRequest);
            ResponseBody<String> responseBody = manageServices.addService(addServiceRequest, userModel.getId());
            logger.info("Add service request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while adding service, {}", exception.getMessage());
            return ResponseEntity.badRequest().body(getFailureResponseBody(exception.getMessage()));
        }
    }

    @GetMapping("/get-user-services")
    public ResponseEntity<ResponseBody<GetServicesResponse>> getUserServices(@AuthenticationPrincipal UserModel userModel) {
        try {
            logger.info("Get user services request received for providerId: {}", userModel.getId());
            ResponseBody<GetServicesResponse> responseBody = manageServices.getUserServicesByProviderId(userModel.getId());
            logger.info("Get user services request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while fetching user services, {}", exception.getMessage());
            ResponseBody<GetServicesResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @DeleteMapping("/delete-service")
    public ResponseEntity<ResponseBody<String>> deleteService(@RequestParam Long serviceId) {
        try {
            logger.info("Delete service request received for serviceId: {}", serviceId);
            ResponseBody<String> responseBody = manageServices.deleteService(serviceId);
            logger.info("Delete service request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while deleting service, {}", exception.getMessage());
            return ResponseEntity.badRequest().body(getFailureResponseBody(exception.getMessage()));
        }
    }

    @PutMapping("/update-service")
    public ResponseEntity<ResponseBody<String>> updateService(
            @Valid @RequestBody UpdateServiceRequest updateServiceRequest,
            @AuthenticationPrincipal UserModel userModel
    ) {
        try {
            logger.info("Update service request received: {}", updateServiceRequest);
            ResponseBody<String> responseBody = manageServices.updateService(updateServiceRequest, userModel.getId());
            logger.info("Update service request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while updating service, {}", exception.getMessage());
            return ResponseEntity.badRequest().body(getFailureResponseBody(exception.getMessage()));
        }
    }

    private ResponseBody<String> getFailureResponseBody(String message) {
        return new ResponseBody<>(FAILURE, null, message);
    }
}