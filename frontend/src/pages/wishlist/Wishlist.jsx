import { HttpStatusCode } from "axios";
import React, { useEffect, useState } from 'react';
import { Container, Spinner, Stack } from 'react-bootstrap';
import EmptyListView from "../../assets/EmptyListView.png";
import AppToast from "../../components/app_toast/AppToast";
import ServiceCard from "../../components/service_card/ServiceCard";
import ServiceDetailsCard from "../../components/service_card/ServiceDetailsCard";
import "./Wishlist.css";
import { UserModal } from "../../components/user_modal/UserModal";
import { useAxios } from '../../context/AxiosContext';
import { ENDPOINTS } from '../../utils/Constants';

export default function Wishlist() {
  const [showToast, setShowToast] = useState(false);
  const [toastMessage, setToastMessage] = useState("");
  const [toastTitle, setToastTitle] = useState("");
  const [providerInfo, setProviderInfo] = useState({});
  const [providerLoading, setProviderInfoLoading] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [userToView, setUserToView] = useState(null);
  const [showUserModal, setShowUserModal] = useState(false);

  const { getRequest, deleteRequest } = useAxios();

  useEffect(() => {
    fetchWishlistedServices();
  }, []);

  const fetchWishlistedServices = async () => {
    setLoading(true);
    try {
      const response = await getRequest(`${ENDPOINTS.GET_WISHLISTED_SERVICES}`, true);
      if (response.status === HttpStatusCode.Ok) {
        setServices(response.data.data.services);
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
      const providerInfoResponse = await getRequest(ENDPOINTS.PROVIDER_DETAILS, true, { providerId: service.providerId })
      const feedbackResponse = await getRequest(ENDPOINTS.GET_FEEDBACK, true, { userId: service.providerId });
      if (providerInfoResponse.status === HttpStatusCode.Ok && feedbackResponse.status === HttpStatusCode.Ok) {
        setProviderInfo(providerInfoResponse.data.data);
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

  const removeFromWishlist = async (wishlist) => {
    try {
      const response = await deleteRequest(ENDPOINTS.DELETE_WISHLIST, true, { wishlistId: wishlist.id });
      if (response.status === HttpStatusCode.Ok) {
        setToastMessage('Removed from wishlist successfully');
        setToastTitle('Success');
        setShowToast(true);
        setSelectedService(null)
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
                <ServiceCard
                  key={idx}
                  service={service}
                  onProfileImageClick={handleViewProfileClicked}
                  onClick={() => handleServiceClick(service)}
                />
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
                key={selectedService.id}
              />
            )}
          </Container>
        </Container>)
      }

      {userToView && (
        <UserModal show={showUserModal} handleClose={handleCloseModal} user={userToView} />
      )}

      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage} />
    </Container>
  );
}
