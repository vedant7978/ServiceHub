import React, { useState, useEffect } from 'react';
import { Container, FormControl, Dropdown, Spinner, Stack } from 'react-bootstrap';
import { HttpStatusCode } from "axios";
import { ENDPOINTS } from '../../utils/Constants';
import { useAxios } from '../../context/AxiosContext';
import AppToast from "../../components/app_toast/AppToast";
import ServiceCard from "../../components/service_card/ServiceCard";
import ServiceDetailsCard from "../../components/service_card/ServiceDetailsCard";
import "./Wishlist.css";
import EmptyListView from "../../assets/EmptyListView.png";

export default function Wishlist() {
  const [showToast, setShowToast] = useState(false);
  const [toastMessage, setToastMessage] = useState("");
  const [toastTitle, setToastTitle] = useState("");
  const [feedbacks, setFeedbacks] = useState([]);
  const [providerInfo, setProviderInfo] = useState({});
  const [providerLoading, setProviderInfoLoading] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(false);

  const { getRequest, deleteRequest } = useAxios();

  useEffect(() => {
    fetchWishlistedServices();
  }, []);

  const fetchWishlistedServices = async () => {
    setLoading(true);
    try {
      const response = await getRequest(`${ENDPOINTS.GET_WISHLISTED_SERVICES}`, true);
      if (response.status === HttpStatusCode.Ok) {
        console.log(response)
        setServices(response.data.data);
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
      const providerInforesponse = await getRequest(ENDPOINTS.PROVIDER_DETAILS, true, { providerId: service.providerId })
      const feedbackResponse = await getRequest(ENDPOINTS.GET_FEEDBACK, true, { userId: service.providerId });
      console.log(providerInforesponse);
      if (providerInforesponse.status === HttpStatusCode.Ok && feedbackResponse.status === HttpStatusCode.Ok) {
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

  const removeFromWishlist = async (wishlistId) => {
    try {
      const response = await deleteRequest(ENDPOINTS.DELETE_WISHLIST, true, { wishlistId });
      if (response.status === HttpStatusCode.Ok) {
        setToastMessage('Removed from wishlist successfully');
        setToastTitle('Success');
        setShowToast(true);
        fetchWishlistedServices(); 
      } else {
        setToastMessage('Error while removing from wishlist');
        setToastTitle('Error');
        setShowToast(true);
      }
    } catch (error) {
      setToastMessage('Error while removing from wishlist');
      setToastTitle('Error');
      setShowToast(true);
    }
  };

  return (
    <Container fluid className="wishlist">
      {loading ? (
        <div className='d-flex align-items-center justify-content-center' style={{ minHeight: "72vh", width: "100%" }}>
          <Spinner animation="border" role="status">
          </Spinner>
        </div>
      ) :
        (<Container className='d-flex result-body' style={{ marginTop: "30px" }}>
          <Container fluid className='child1 flex-column'>
            {services.length > 0 ? (
              services.map((service, idx) => (
                <ServiceCard key={idx} service={service} onClick={() => handleServiceClick(service)} />
              ))
            ) : (
              <Container
                fluid
                className="empty-services-view d-flex align-items-center justify-content-center pb-5"
              >
                <div>
                  <Stack className="align-items-center" gap={3}>
                    <img src={EmptyListView} alt="No Services" width="200px" height="200px" />
                    <div className="empty-services-text">No Services Added</div>
                  </Stack>
                </div>
              </Container>
            )
            }
          </Container>

          <Container fluid className='child2'>

            {selectedService && (
              <ServiceDetailsCard
                selectedService={selectedService}
                providerLoading={providerLoading}
                providerInfo={providerInfo}
                rightIconOnclick={removeFromWishlist}
                currentPage="wishlist"
                showToastMessage={(message, title) => {
                  setToastMessage(message);
                  setToastTitle(title);
                  setShowToast(true);
                }}
              />
            )}
          </Container>
        </Container>)
      }
      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage} />
    </Container>
  );
}
