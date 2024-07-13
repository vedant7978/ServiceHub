import { HttpStatusCode } from "axios";
import React, { useEffect, useState } from "react";
import { Button, Container, Dropdown, Stack } from "react-bootstrap";
import "./AddUpdateServiceCard.css";
import { useAxios } from "../../../context/AxiosContext";
import { ENDPOINTS, ServiceType } from "../../../utils/Constants";
import AppToast from "../../app_toast/AppToast";

export const AddUpdateServiceCard = ({ service, onRefreshServices }) => {
  const isAddingService = service === null;
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [perHourRate, setPerHourRate] = useState("");
  const [type, setType] = useState(service ? service.type : ServiceType["1"]);
  const [hasServiceChanged, setHasServiceChanged] = useState(false);
  const [isSubmitButtonDisabled, setIsSubmitButtonDisabled] = useState();
  const [showToast, setShowToast] = useState(false);
  const [toastTitle, setToastTitle] = useState("");
  const [toastMessage, setToastMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState(null);
  const { postRequest, putRequest } = useAxios();

  useEffect(() => {
    if (!service) return;
    setName(service.name);
    setDescription(service.description);
    setPerHourRate(service.perHourRate);
    setType(service.type);
  }, [service]);

  useEffect(() => {
    if (isAddingService) {
      setHasServiceChanged(name !== "" && description !== "" && perHourRate !== "")
    } else {
      setHasServiceChanged(
        name !== service.name ||
        description !== service.description ||
        Number(perHourRate) !== service.perHourRate ||
        type !== service.type
      )
    }
  }, [name, description, perHourRate, type, service, isAddingService])

  useEffect(() => {
    setIsSubmitButtonDisabled(!hasServiceChanged)
  }, [hasServiceChanged]);

  const handleSubmitService = async (e) => {
    e.preventDefault()
    if (isNaN(perHourRate)) {
      setToastTitle("Failure")
      setToastMessage(`Please provide valid number for per hour rate`)
      setShowToast(true)
      return
    }
    isAddingService ? addService() : updateService();
  }

  const addService = async () => {
    if (!validateService()) return;
    const service = { name: name, description: description, perHourRate: perHourRate, type: type };
    try {
      const response = await postRequest(ENDPOINTS.ADD_SERVICE, true, service);
      if (response.status === HttpStatusCode.Ok) {
        setIsSubmitButtonDisabled(true)
        onRefreshServices({title: "Success", message: "Successfully added the service."})
      } else {
        setToastTitle("Failure")
        setToastMessage(`Failed to add service: ${response.data.message}`)
        setShowToast(true)
      }
    } catch (error) {
      setToastTitle("Failure")
      setToastMessage(`Failed to add service: ${error}`)
      setShowToast(true)
    }
  }

  const updateService = async () => {
    if (!validateService()) return;
    const serviceToUpdate = {
      id: service.id,
      name: name,
      description: description,
      perHourRate: perHourRate,
      type: type
    };
    try {
      const response = await putRequest(ENDPOINTS.UPDATE_SERVICE, true, serviceToUpdate);
      if (response.status === HttpStatusCode.Ok) {
        setIsSubmitButtonDisabled(true)
        onRefreshServices({title: "Success", message: "Successfully updated the service."})
      } else {
        setToastTitle("Failure")
        setToastMessage(`Failed to update service: ${response.data.message}`)
        setShowToast(true)
      }
    } catch (error) {
      setToastTitle("Failure")
      setToastMessage(`Failed to update service: ${error}`)
      setShowToast(true)
    }
  }

  const validateService = () => {
    if (!name || name === "") {
      setErrorMessage("Please enter valid name of the service");
      return false;
    }
    if (!description || description === "") {
      setErrorMessage("Please enter valid description of the service");
      return false;
    }
    if (!perHourRate || perHourRate === "") {
      setErrorMessage("Please enter valid per hour rate for the service");
      return false;
    }
    if (!type || type === "") {
      setErrorMessage("Please enter valid type of the service");
      return false;
    }
    setErrorMessage(null);
    return true;
  }

  const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
    <Stack
      ref={ref}
      direction="horizontal"
      className="d-flex justify-content-between pt-1 pb-1 ps-2 pe-2 h-100"
      onClick={(e) => {
        e.preventDefault();
        onClick(e);
      }}
      gap={2}
    >
      <div>{children}</div>
      <div>&#x25bc;</div>
    </Stack>
  ));

  const handleHistoryTypeDropdownClick = (eventKey) => {
    for (const key of Object.keys(ServiceType)) {
      if (key === eventKey) {
        setType(ServiceType[key])
        break;
      }
    }
  };

  return (
    <Container fluid className="p-4 add-update-service-card d-flex align-items-center mt-2">
      <Stack gap={4}>
        <div>
          <Stack gap={2}>
            <div className="add-update-service-title d-flex justify-content-center">
              {isAddingService ? "Add Service" : "Edit Service"}
            </div>
            <input
              className="add-update-service-name pt-1 pb-1 ps-2 pe-2 mt-2"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Name"
            />
            <textarea
              className="add-update-service-description pt-1 pb-1 ps-2 pe-2 mt-2"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Description"
            />
            <input
              className="add-update-service-per-hour-rate pt-1 pb-1 ps-2 pe-2 mt-2"
              value={perHourRate}
              onChange={(e) => setPerHourRate(e.target.value)}
              placeholder="Per hour rate ($)"
            />
            <Dropdown onSelect={handleHistoryTypeDropdownClick} className="mb-3 add-update-service-type-dropdown">
              <Dropdown.Toggle as={CustomToggle} id="dropdown-custom-components">
                {type}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item eventKey="1"> HomeServices</Dropdown.Item>
                <Dropdown.Item eventKey="2"> PersonalServices</Dropdown.Item>
                <Dropdown.Item eventKey="3"> ProfessionalServices</Dropdown.Item>
                <Dropdown.Item eventKey="4"> EducationalServices</Dropdown.Item>
                <Dropdown.Item eventKey="5"> TechnicalServices</Dropdown.Item>
                <Dropdown.Item eventKey="6"> EventServices</Dropdown.Item>
                <Dropdown.Item eventKey="7"> TransportationServices</Dropdown.Item>
                <Dropdown.Item eventKey="8"> HealthAndWellness</Dropdown.Item>
                <Dropdown.Item eventKey="9"> CreativeServices</Dropdown.Item>
                <Dropdown.Item eventKey="10"> LegalAndFinancialServices</Dropdown.Item>
                <Dropdown.Item eventKey="11"> Plumbing</Dropdown.Item>
                <Dropdown.Item eventKey="12"> Electrician</Dropdown.Item>
                <Dropdown.Item eventKey="13"> Other</Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          </Stack>
        </div>

        <Stack>
          {errorMessage && (
            <div className="error-message-text">{errorMessage}</div>
          )}

          <Button
            disabled={isSubmitButtonDisabled}
            className="add-update-service-submit-button mt-5"
            onClick={handleSubmitService}>
            {isAddingService ? "Add Service" : "Update Service"}
          </Button>
        </Stack>
      </Stack>
      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage} />
    </Container>
  );
}