import React, { useState, useEffect, useCallback } from 'react';
import { Container, FormControl, Dropdown, Spinner } from 'react-bootstrap';
import { ENDPOINTS } from '../../utils/Constants';
import { useAxios } from '../../context/AxiosContext';
import AppToast from "../../components/app_toast/AppToast";
import ServiceCard from "../../components/service_card/ServiceCard";
import ServiceDetailsCard from "../../components/service_card/ServiceDetailsCard";
import debounce from 'lodash/debounce';
import "./Dashboard.css";
import { HttpStatusCode } from "axios";

export default function Dashboard() {
  const [showToast, setShowToast] = useState(false);
  const [toastMessage, setToastMessage] = useState("");
  const [toastTitle, setToastTitle] = useState("");
  const [feedbacks, setFeedbacks] = useState([]);
  const [providerInfo, setProviderInfo] = useState({});
  const [providerLoading, setProviderInfoLoading] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const [services, setServices] = useState([]);
  const [filteredServices, setFilteredServices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [serviceTypes, setServiceTypes] = useState([]);
  const [sortCriteria, setSortCriteria] = useState("");

  const { getRequest, postRequest } = useAxios();

  useEffect(() => {
    fetchServices('');
  }, []);

  const extractServiceTypes = (services) => {
    const types = [...new Set(services.map(service => service.type))];
    setServiceTypes(types);
  };

  const addToWishlist = async (serviceId) => {
    setProviderInfoLoading(true);
    try {
      const requestData = { serviceId: serviceId }
      const Response = await postRequest(ENDPOINTS.ADD_WISHLIST, true, requestData);
      if (Response.status === HttpStatusCode.Ok) {
        setToastMessage('Added to wishlist');
        setToastTitle('success');
        setShowToast(true);
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
      console.log(response.status)
      if (response.status === HttpStatusCode.Ok) {
        const fetchedServices = response.data.data.services;
        setServices(fetchedServices);
        extractServiceTypes(fetchedServices);
        filterAndSortServices(fetchedServices, sortCriteria);
        if (fetchedServices.length > 0) {
          handleServiceClick(fetchedServices[0]);
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
      const providerInforesponse = await getRequest(ENDPOINTS.PROVIDER_DETAILS, true, { providerId: service.providerId });
      const feedbackResponse = await getRequest(ENDPOINTS.GET_FEEDBACK, true, { userId: service.providerId });
      if (providerInforesponse.status && feedbackResponse.status === HttpStatusCode.Ok) {
        setFeedbacks(feedbackResponse.data.data.feedbacks);
        setProviderInfo(providerInforesponse.data.data.provider);
      } else {
        setToastMessage('Error fetching feedbacks.');
        setToastTitle('Error');
        setShowToast(true);
      }
    } catch (error) {
      setToastMessage('Error fetching feedbacks.');
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
        <Dropdown className="mb-3">
          <Dropdown.Toggle variant="outline-secondary">
            Sort By
          </Dropdown.Toggle>
          <Dropdown.Menu>
            {serviceTypes.map((type, idx) => (
              <Dropdown.Item key={idx} onClick={() => handleSortChange(type)}>
                {type}
              </Dropdown.Item>
            ))}
          </Dropdown.Menu>
        </Dropdown>
      </Container>
      <Container className='d-flex result-body'>
        <Container fluid className='child1 flex-column'>
          {loading ? (
            <Spinner animation="border" role="status">
              <span className="sr-only">Loading...</span>
            </Spinner>
          ) : (
            filteredServices.map((service, idx) => (
              <ServiceCard key={idx} service={service} onClick={() => handleServiceClick(service)} />
            ))
          )}
        </Container>

        <Container fluid className='child2'>
          {selectedService && (
            <ServiceDetailsCard
              selectedService={selectedService}
              providerLoading={providerLoading}
              feedbacks={feedbacks}
              providerInfo={providerInfo}
              rightIconOnclick={addToWishlist}
              currentPage="dashboard"
            />
          )}
        </Container>
      </Container>
      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage} />
    </Container>
  );
}
