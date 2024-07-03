import { HttpStatusCode } from "axios";
import React, { useEffect, useState } from 'react';
import { Container, Stack } from "react-bootstrap";
import "./ManageServices.css";
import EmptyListView from "../../assets/EmptyListView.png";
import AppToast from "../../components/app_toast/AppToast";
import { ConfirmationPopup } from "../../components/ConfirmationPopup";
import { AddUpdateServiceCard } from "../../components/manage_services/add_update_service_card/AddUpdateServiceCard";
import { UserServiceCard } from "../../components/manage_services/user_service_card/UserServiceCard";
import { useAxios } from "../../context/AxiosContext";
import { ENDPOINTS } from "../../utils/Constants";

export default function ManageServices() {

  const [services, setServices] = useState([]);
  const [selectedServiceToEdit, setSelectedServiceToEdit] = useState(null);
  const [showToast, setShowToast] = useState(false);
  const [toastTitle, setToastTitle] = useState("");
  const [toastMessage, setToastMessage] = useState("");
  const [serviceIdToDelete, setServiceIdToDelete] = useState(0);
  const [isDeleteServiceDialogVisible, setIsDeleteServiceDialogVisible] = useState(false);
  const [serviceToAddKey, setServiceToAddKey] = useState(0);
  const { getRequest, deleteRequest } = useAxios();

  useEffect(() => {
    loadServices();
  }, []);

  const loadServices = async () => {
    try {
      const response = await getRequest(ENDPOINTS.GET_SERVICES, true);
      const services = response.data.data.services;
      setServices(services);
      setServiceToAddKey(prev => prev + 1)
      setSelectedServiceToEdit(null)
    } catch (error) {
      console.log("Failed to load services", error);
    }
  }

  const onServiceAddedOrUpdated = async ({ title, message }) => {
    await loadServices()
    setToastTitle(title)
    setToastMessage(message)
    setShowToast(true)
  }

  const handleDeleteService = (serviceId) => {
    setServiceIdToDelete(serviceId)
    setIsDeleteServiceDialogVisible(true)
  }

  const deleteService = async () => {
    if (!serviceIdToDelete) return;
    try {
      const response = await deleteRequest(ENDPOINTS.DELETE_SERVICE, true, { serviceId: serviceIdToDelete });
      if (response.status === HttpStatusCode.Ok) {
        await onServiceAddedOrUpdated({ title: "Success", message: "Successfully deleted service." })
        setServiceIdToDelete(null)
      }
    } catch (error) {
      console.log("Failed to delete service", error);
    }
  }

  return (
    <Container fluid className="manage-services px-0 align-items-center">
      <Container>
        <div className="my-services-title pt-3">My Services</div>
        <Stack
          direction="horizontal"
          className="d-flex justify-content-between align-items-center flex-md-row align-items-md-start flex-column"
          gap={5}
        >
          <div className="d-flex w-100">
            <Stack direction="vertical" className="services-list-view p-3">
              {
                services.length > 0 ? (
                  services.map(service => (
                    <UserServiceCard
                      service={service}
                      isSelected={selectedServiceToEdit ? selectedServiceToEdit.id === service.id : false}
                      onEditClicked={() => setSelectedServiceToEdit(service)}
                      onDeleteClicked={() => handleDeleteService(service.id)}
                      key={service.id}
                    />
                  ))
                ) : (
                  <Container
                    fluid
                    className="empty-services-view d-flex align-items-center justify-content-center pb-5">
                    <div>
                      <Stack className="align-items-center" gap={3}>
                        <img src={EmptyListView} alt="NavigateLeft" width="200px" height="200px"/>
                        <div className="empty-contracts-text">No Services Added</div>
                      </Stack>
                    </div>
                  </Container>
                )
              }
            </Stack>
          </div>

          <div>
            <Container fluid className="add-feedback-card d-flex align-items-center mt-2">
              <AddUpdateServiceCard
                service={selectedServiceToEdit}
                onRefreshServices={onServiceAddedOrUpdated}
                key={selectedServiceToEdit ? selectedServiceToEdit.id : serviceToAddKey}
              />
            </Container>
          </div>
        </Stack>
      </Container>

      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage}/>

      {isDeleteServiceDialogVisible && (
        <ConfirmationPopup
          message="Are you sure you delete the contract?"
          onConfirm={async () => {
            setIsDeleteServiceDialogVisible(false)
            await deleteService()
            setServiceIdToDelete(null)
          }}
          onCancel={() => {
            setIsDeleteServiceDialogVisible(false)
            setServiceIdToDelete(null)
          }}
        />
      )}
    </Container>
  );
};
