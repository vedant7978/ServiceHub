package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.factory.service.ServiceFactory;
import com.dalhousie.servicehub.model.UserModel;
import com.dalhousie.servicehub.request.ContractRequest;
import com.dalhousie.servicehub.response.GetProviderResponse;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.service.dashboard_services.DashboardService;
import com.dalhousie.servicehub.util.ResponseBody;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger logger = LogManager.getLogger(DashboardController.class);
    private final DashboardService dashboardService;

    public DashboardController(ServiceFactory serviceFactory) {
        dashboardService = serviceFactory.getDashboardService();
    }

    @GetMapping("/all-services")
    public ResponseEntity<ResponseBody<GetServicesResponse>> getAllServices() {
        try {
            logger.info("Get all services request received");
            ResponseBody<GetServicesResponse> responseBody = dashboardService.getAllServices();
            logger.info("Get all services request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while fetching all services, {}", exception.getMessage());
            ResponseBody<GetServicesResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @GetMapping("/services-by-type")
    public ResponseEntity<ResponseBody<GetServicesResponse>> getServicesByType(@RequestParam ServiceType type) {
        try {
            logger.info("Get services by type request received for type: {}", type);
            ResponseBody<GetServicesResponse> responseBody = dashboardService.getServicesByType(type);
            logger.info("Get services by type request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while fetching services by type, {}", exception.getMessage());
            ResponseBody<GetServicesResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @GetMapping("/search-services")
    public ResponseEntity<ResponseBody<GetServicesResponse>> searchServicesByName(@RequestParam String name) {
        try {
            logger.info("Search services by name request received for name: {}", name);
            ResponseBody<GetServicesResponse> responseBody = dashboardService.searchServicesByName(name);
            logger.info("Search services by name request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while searching services by name, {}", exception.getMessage());
            ResponseBody<GetServicesResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @GetMapping("/provider-details")
    public ResponseEntity<ResponseBody<GetProviderResponse>> getUserDetailsByProviderId(@RequestParam Long providerId) {
        try {
            logger.info("Get user details by provider ID request received for provider ID: {}", providerId);
            ResponseBody<GetProviderResponse> responseBody = dashboardService.getProviderDetailsById(providerId);
            logger.info("Get user details by provider ID request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while fetching user details by provider ID, {}", exception.getMessage());
            ResponseBody<GetProviderResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }

    @PostMapping("/request-service")
    public ResponseEntity<ResponseBody<String>> requestService(@Valid @RequestBody ContractRequest request,
                                                               @AuthenticationPrincipal UserModel userModel) {
        try {
            logger.info("Request service request received");
            ResponseBody<String> responseBody = dashboardService.requestService(request,userModel.getId());
            logger.info("Request service request success");
            return ResponseEntity.ok(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while requesting service, {}", exception.getMessage());
            ResponseBody<String> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }
}
