import { HttpStatusCode } from "axios";
import debounce from 'lodash/debounce';
import React, { useCallback, useEffect, useState } from 'react';
import { Container, Dropdown, FormControl, Spinner, Stack } from 'react-bootstrap';
import EmptyListView from "../../assets/EmptyListView.png";
import AppToast from "../../components/app_toast/AppToast";
import ServiceCard from "../../components/service_card/ServiceCard";
import ServiceDetailsCard from "../../components/service_card/ServiceDetailsCard";
import { UserModal } from "../../components/user_modal/UserModal";
import { useAxios } from '../../context/AxiosContext';
import { ENDPOINTS } from '../../utils/Constants';
import "./Dashboard.css";

export default function Dashboard() {
  const [showToast, setShowToast] = useState(false);
  const [toastMessage, setToastMessage] = useState("");
  const [toastTitle, setToastTitle] = useState("");
  const [providerInfo, setProviderInfo] = useState({});
  const [providerLoading, setProviderInfoLoading] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const [services, setServices] = useState([]);
  const [filteredServices, setFilteredServices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [serviceTypes, setServiceTypes] = useState([]);
  const [sortCriteria, setSortCriteria] = useState("");
  const [userToView, setUserToView] = useState(null);
  const [showUserModal, setShowUserModal] = useState(false);
  const { getRequest, postRequest } = useAxios();

  useEffect(() => {
    debouncedFetchServices('')
  }, []);

  const extractServiceTypes = (services) => {
    const types = [...new Set(services.map(service => service.type))];
    setServiceTypes(types);
  };

  const addToWishlist = async (service) => {
    if (service.addedToWishlist) {
      setToastMessage('Service is already in the wishlist');
      setToastTitle('Info');
      setShowToast(true);
      return;
    }

    setProviderInfoLoading(true);
    try {
      const requestData = { serviceId: service.id };
      const Response = await postRequest(ENDPOINTS.ADD_WISHLIST, true, requestData);
      if (Response.status === HttpStatusCode.Ok) {
        setToastMessage('Added to wishlist');
        setToastTitle('Success');
        setShowToast(true);
        setSelectedService({...service, addedToWishlist: true });
      } else {
        setToastMessage('Error while adding to wishlist');
        setToastTitle('Error');
        setShowToast(true);
      }
    } catch (error) {
      setToastMessage('Error while adding to wishlist');
      setToastTitle('Error');
      setShowToast(true);
    }
    setProviderInfoLoading(false);
  };

  const fetchServices = async (query) => {
    setLoading(true);
    try {
      const response = await getRequest(`${ENDPOINTS.GET_SEARCH_SERVICES}?name=${query}`, true);
      if (response.status === HttpStatusCode.Ok) {
        const fetchedServices = response.data.data.services;
        setServices(fetchedServices);
        extractServiceTypes(fetchedServices);
        filterAndSortServices(fetchedServices, sortCriteria);
        if (fetchedServices.length > 0) {
          await handleServiceClick(fetchedServices[0]);
        }
      } else {
        setToastMessage('Error fetching services.');
        setToastTitle('Error');
        setShowToast(true);
      }
    } catch (error) {
      setToastMessage('Error fetching services.');
      setToastTitle('Error');
      setShowToast(true);
    }
    setLoading(false);
  };

  const handleServiceClick = async (service) => {
    setSelectedService(service);
    setProviderInfoLoading(true);
    try {
      const providerInfoResponse = await getRequest(ENDPOINTS.PROVIDER_DETAILS, true, { providerId: service.providerId });
      if (providerInfoResponse.status === HttpStatusCode.Ok) {
        setProviderInfo(providerInfoResponse.data.data);
      } else {
        setToastMessage('Error fetching user Details.');
        setToastTitle('Error');
        setShowToast(true);
      }
    } catch (error) {
      setToastMessage('Error fetching user Details.');
      setToastTitle('Error');
      setShowToast(true);
    }
    setProviderInfoLoading(false);
  };

  const debouncedFetchServices = useCallback(debounce(fetchServices, 500), []);

  const handleSearchChange = (event) => {
    const query = event.target.value;
    setSearchQuery(query);
    debouncedFetchServices(query);
  };

  const handleSortChange = (type) => {
    setSortCriteria(type);
    filterAndSortServices(services, type);
  };

  const filterAndSortServices = (services, type) => {
    const filtered = type ? services.filter(service => service.type === type) : services;
    setFilteredServices(filtered);
  };

  const handleViewProfileClicked = async (userId) => {
    try {
      console.log(userId)
      const response = await getRequest(ENDPOINTS.PROVIDER_DETAILS, true, { providerId: userId });
      if (response.status === HttpStatusCode.Ok) {
        setUserToView(response.data.data);
        setShowUserModal(true);
      } else {
        setToastTitle('Error');
        setToastMessage('Error fetching user Details.');
        setShowToast(true);
      }
    } catch (error) {
      setToastTitle('Error');
      setToastMessage('Error fetching user Details.');
      setShowToast(true);
    }
  }

  const handleCloseModal = () => {
    setShowUserModal(false);
    setUserToView(null);
  };

  return (
    <Container fluid className="dashboard">
      <Container style={{ padding: '24px' }}>
        <FormControl
          type="text"
          placeholder="Search"
          className="mb-3"
          value={searchQuery}
          onChange={handleSearchChange}
        />
        {loading ? (
          <div className='d-flex align-items-center justify-content-center' style={{ minHeight: "72vh" }}>
            <Spinner animation="border" role="status">
            </Spinner>
          </div>
        ) : (
          <>
            {serviceTypes.length > 0 ? (
              <>
                <Dropdown className="mb-3">
                  <Dropdown.Toggle variant="outline-secondary">
                    Sort By
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    <Dropdown.Item onClick={() => handleSortChange('')}>
                      All services
                    </Dropdown.Item>
                    {serviceTypes.map((type, idx) => (
                      <Dropdown.Item key={idx} onClick={() => handleSortChange(type)}>
                        {type}
                      </Dropdown.Item>
                    ))}
                  </Dropdown.Menu>
                </Dropdown>
                <Container className='d-flex result-body' style={{ padding: '0px' }}>
                  <Container fluid className='child1 flex-column'>
                    {filteredServices.map((service, idx) => (
                      <ServiceCard
                        key={idx}
                        service={service}
                        onProfileImageClick={handleViewProfileClicked}
                        onClick={() => handleServiceClick(service)}
                      />
                    ))}
                  </Container>
                  <Container fluid className='child2' style={{ padding: '0px' }}>
                    {selectedService && (
                      <ServiceDetailsCard
                        selectedService={selectedService}
                        providerLoading={providerLoading}
                        providerInfo={providerInfo}
                        rightIconOnclick={addToWishlist}
                        currentPage="dashboard"
                        showToastMessage={(message,title) => {
                          setToastMessage(message);
                          setToastTitle(title);
                          setShowToast(true);
                        }}
                        refreshOnRequested={() => {
                          setFilteredServices(filteredServices.map((service) => {
                            if (service.id === selectedService.id)
                              return {...service, requested: true }
                            return service;
                          }))
                        }}
                        key={selectedService.id}
                      />
                    )}
                  </Container>
                </Container>
              </>
            ) : (
              <Container
                fluid
                className="empty-services-view d-flex align-items-center justify-content-center pb-5"
              >
                <div>
                  <Stack className="align-items-center" gap={3}>
                    <img src={EmptyListView} alt="NavigateLeft" width="200px" height="200px" />
                    <div className="empty-services-text">No Services Added</div>
                  </Stack>
                </div>
              </Container>
            )}
          </>
        )}
      </Container>

      {userToView && (
        <UserModal show={showUserModal} handleClose={handleCloseModal} user={userToView} />
      )}

      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage} />
    </Container>
  );
}
