package com.dalhousie.servicehub.service.dashboard_services;

import com.dalhousie.servicehub.enums.ServiceType;
import com.dalhousie.servicehub.request.ContractRequest;
import com.dalhousie.servicehub.response.GetProviderResponse;
import com.dalhousie.servicehub.response.GetServicesResponse;
import com.dalhousie.servicehub.util.ResponseBody;

public interface DashboardServices {

    /**
     * Provides all services created by all users other than logged-in user
     * @return GetServicesResponse instance containing list of services
     */
    ResponseBody<GetServicesResponse> getAllServices();

    /**
     * Provides all services filtered by requesting type created by all users other than logged-in user
     * @param type Type of the service
     * @return GetServicesResponse instance containing list of services
     */
    ResponseBody<GetServicesResponse> getServicesByType(ServiceType type);

    /**
     * Provides all services filtered by requesting name created by all users other than logged-in user
     * @param name Name of the service
     * @return GetServicesResponse instance containing list of services
     */
    ResponseBody<GetServicesResponse> searchServicesByName(String name);

    /**
     * Provide user details of the requesting service provider id
     * @param providerId ID of the service providing user
     * @return GetProviderResponse instance containing user details
     */
    ResponseBody<GetProviderResponse> getProviderDetailsById(Long providerId);

    /**
     * Requests a service with the service ID.
     * @param contractRequest The request object containing service ID and address.
     * @param userId The ID of the user requesting the service.
     * @return A ResponseBody object containing a success message if the service is requested successfully.
     */
    ResponseBody<String> requestService(ContractRequest contractRequest, Long userId);
}
