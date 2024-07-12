package com.dalhousie.servicehub.service.dashboard_services;
import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.request.AddServiceRequest;
import com.dalhousie.servicehub.request.UpdateServiceRequest;
import com.dalhousie.servicehub.response.GetProviderResponse;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.util.ResponseBody;
import org.hibernate.service.spi.ServiceException;

import javax.management.ServiceNotFoundException;
import java.util.List;

public interface DashboardServices {

    ResponseBody<GetServicesResponse> getAllServices();
    ResponseBody<GetServicesResponse> getServicesByType(ServiceType type);
    ResponseBody<GetServicesResponse> searchServicesByName(String name);
    ResponseBody<GetProviderResponse> getProviderDetailsById(Long providerId); // Add this method

}


