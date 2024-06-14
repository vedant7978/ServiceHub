package com.dalhousie.servicehub.service.user_services;
import com.dalhousie.servicehub.request.AddServiceRequest;
import com.dalhousie.servicehub.request.UpdateServiceRequest;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.util.ResponseBody;
import org.hibernate.service.spi.ServiceException;

import javax.management.ServiceNotFoundException;

public interface ManageService {

     ResponseBody<Object> addService(AddServiceRequest addServiceRequest) throws ServiceException;
     ResponseBody<GetServicesResponse> getUserServicesByProviderId(Long providerId);
     ResponseBody<Object> deleteService(Long serviceId) throws ServiceNotFoundException;
     ResponseBody<Object> updateService(UpdateServiceRequest updateServiceRequest);
}


