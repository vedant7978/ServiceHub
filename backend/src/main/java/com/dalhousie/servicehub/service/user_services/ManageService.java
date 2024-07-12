package com.dalhousie.servicehub.service.user_services;

import com.dalhousie.servicehub.request.AddServiceRequest;
import com.dalhousie.servicehub.request.UpdateServiceRequest;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.util.ResponseBody;

public interface ManageService {

     ResponseBody<Object> addService(AddServiceRequest addServiceRequest, Long providerId);
     ResponseBody<GetServicesResponse> getUserServicesByProviderId(Long providerId);
     ResponseBody<Object> deleteService(Long serviceId);
     ResponseBody<Object> updateService(UpdateServiceRequest updateServiceRequest, Long providerId);
}


