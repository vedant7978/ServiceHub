package com.dalhousie.servicehub.service.user_services;

import com.dalhousie.servicehub.request.AddServiceRequest;
import com.dalhousie.servicehub.request.UpdateServiceRequest;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.util.ResponseBody;

public interface ManageService {

     /**
      * Add a service for the provided id
      * @param addServiceRequest AddServiceRequest instance
      * @param providerId ID of the user providing service
      * @return ResponseBody object for String representing api result
      */
     ResponseBody<String> addService(AddServiceRequest addServiceRequest, Long providerId);

     /**
      * Provides all services added by the requesting id
      * @param providerId ID of the service providing user
      * @return ResponseBody object for GetServicesResponse
      */
     ResponseBody<GetServicesResponse> getUserServicesByProviderId(Long providerId);

     /**
      * Delete a service for the provided ID
      * @param serviceId ID of the service to delete
      * @return ResponseBody object for String representing api result
      */
     ResponseBody<String> deleteService(Long serviceId);

     /**
      * Update a service for the provided ID
      * @param updateServiceRequest UpdateServiceRequest instance
      * @param providerId ID of the service to update
      * @return ResponseBody object for String representing api result
      */
     ResponseBody<String> updateService(UpdateServiceRequest updateServiceRequest, Long providerId);
}
