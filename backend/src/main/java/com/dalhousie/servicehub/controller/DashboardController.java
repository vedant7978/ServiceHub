package com.dalhousie.servicehub.controller;

import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.service.dashboard_services.DashboardServices;
import com.dalhousie.servicehub.util.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dalhousie.servicehub.util.ResponseBody.ResultType.FAILURE;

@RestController
@RequestMapping("/api/service")
public class DashboardController {

    private static final Logger logger = LogManager.getLogger(ServiceController.class);

    @Autowired
    private DashboardServices dashboardServices;

    @GetMapping("/all-services")
    public ResponseEntity<ResponseBody<GetServicesResponse>> getAllServices() {
        try {
            logger.info("Get all services request received");
            ResponseBody<GetServicesResponse> responseBody = dashboardServices.getAllServices();
            logger.info("Get all services request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while fetching all services, {}", exception.getMessage());
            ResponseBody<GetServicesResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }

    @GetMapping("/services-by-type")
    public ResponseEntity<ResponseBody<GetServicesResponse>> getServicesByType(@RequestParam ServiceType type) {
        try {
            logger.info("Get services by type request received for type: {}", type);
            ResponseBody<GetServicesResponse> responseBody = dashboardServices.getServicesByType(type);
            logger.info("Get services by type request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while fetching services by type, {}", exception.getMessage());
            ResponseBody<GetServicesResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }
    @GetMapping("/search-services")
    public ResponseEntity<ResponseBody<GetServicesResponse>> searchServicesByName(@RequestParam String name) {
        try {
            logger.info("Search services by name request received for name: {}", name);
            ResponseBody<GetServicesResponse> responseBody = dashboardServices.searchServicesByName(name);
            logger.info("Search services by name request success");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch (Exception exception) {
            logger.error("Unexpected error occurred while searching services by name, {}", exception.getMessage());
            ResponseBody<GetServicesResponse> body = new ResponseBody<>(FAILURE, null, exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
    }
}
