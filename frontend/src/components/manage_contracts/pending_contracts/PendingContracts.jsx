import React, { useEffect, useState } from "react";
import { Container, Stack } from "react-bootstrap";
import "./PendingContracts.css";
import EmptyListView from "../../../assets/EmptyListView.png";
import { useAxios } from "../../../context/AxiosContext";
import { ENDPOINTS } from "../../../utils/Constants";
import AppToast from "../../app_toast/AppToast";
import { ConfirmationPopup } from "../../ConfirmationPopup";
import { ESignatureForm } from "../ESignatureForm";
import { PendingContractCard } from "../pending_contract_card/PendingContractCard";

export const PendingContracts = () => {
  const [showESignatureForm, setShowESignatureForm] = useState(false);
  const [isRejectContractConfirmationVisible, setIsRejectContractConfirmationVisible] = useState(false);
  const [selectedContract, setSelectedContract] = useState(null);
  const [showToast, setShowToast] = useState(false);
  const [toastTitle, setToastTitle] = useState("");
  const [toastMessage, setToastMessage] = useState("");
  const [contracts, setContracts] = useState([]);
  const { getRequest, postRequest } = useAxios();

  useEffect(() => {
    loadContracts()
  }, []);

  const loadContracts = async () => {
    try {
      const response = await getRequest(ENDPOINTS.GET_PENDING_CONTRACTS, true);
      setContracts(response.data.data.contracts);
    } catch (error) {
      console.log("Failed to load contracts", error);
    }
  }

  const acceptSelectedContract = async () => {
    try {
      const requestData = { contractId: selectedContract.id }
      const response = await postRequest(ENDPOINTS.ACCEPT_CONTRACT, true, requestData);
      const data = response.data.data;
      if (data) {
        setToastTitle("Contract accepted successfully")
        setToastMessage("You can view more details about requester from history.")
      } else {
        setToastTitle("Error")
        setToastMessage("Failed to accept contract.")
      }
    } catch (error) {
      setToastTitle("Error")
      setToastMessage(`Failed to accept contract: ${error}`)
    } finally {
      setShowToast(true)
      setShowESignatureForm(false)
      await loadContracts()
    }
  }

  const rejectSelectedContract = async () => {
    try {
      const requestData = { contractId: selectedContract.id }
      const response = await postRequest(ENDPOINTS.REJECT_CONTRACT, true, requestData);
      const data = response.data.data;
      if (data) {
        setToastTitle("Contract rejected successfully")
        setToastMessage("You can view more details about requester from history.")
      } else {
        setToastTitle("Error")
        setToastMessage("Failed to reject contract.")
      }
    } catch (error) {
      setToastTitle("Error")
      setToastMessage(`Failed to reject contract: ${error}`)
    } finally {
      setShowToast(true)
      await loadContracts()
    }
  }

  const handleAcceptContract = (shouldShow, contract) => {
    setShowESignatureForm(shouldShow);
    setSelectedContract(contract)
  }

  const handleRejectContract = (shouldShow, contract) => {
    setIsRejectContractConfirmationVisible(shouldShow);
    setSelectedContract(contract)
  }

  return (
    <Container className="pending-contracts-container pt-4 pb-4 d-flex justify-content-start">
      <Stack direction="horizontal" className="align-items-start">
        {
          contracts.length > 0 ? (
            contracts.map(contract => (
              <PendingContractCard
                contract={contract}
                showAcceptDialog={() => setShowESignatureForm(true)}
                showRejectDialog={() => setIsRejectContractConfirmationVisible(true)}
                onSelectContract={(contract) => setSelectedContract(contract)}
                key={contract.id}
              />
            ))
          ) : (
            <Container fluid className="empty-contracts-view d-flex align-items-center justify-content-center">
              <div>
                <Stack className="align-items-center" gap={3}>
                  <img src={EmptyListView} alt="NavigateLeft" width="200px" height="200px"/>
                  <div className="empty-contracts-text">No pending contracts available</div>
                </Stack>
              </div>
            </Container>
          )
        }
      </Stack>

      {showESignatureForm && (
        <ESignatureForm
          show={showESignatureForm}
          onHide={() => handleAcceptContract(false, null)}
          onSubmit={async () => {
            await acceptSelectedContract()
            handleAcceptContract(false, null)
          }}
          contract={selectedContract}
        />
      )}

      {isRejectContractConfirmationVisible && (
        <ConfirmationPopup
          message="Are you sure you reject the contract?"
          onConfirm={async () => {
            await rejectSelectedContract()
            handleRejectContract(false, null)
          }}
          onCancel={() => handleRejectContract(false, null)}
        />
      )}

      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage}/>
    </Container>
  );
}