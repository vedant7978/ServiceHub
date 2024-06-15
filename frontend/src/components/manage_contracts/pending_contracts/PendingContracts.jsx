import React, { useEffect, useState } from "react";
import { Container, Stack } from "react-bootstrap";
import "./PendingContracts.css";
import EmptyListView from "../../../assets/EmptyListView.png";
import { useAxios } from "../../../context/AxiosContext";
import { ENDPOINTS } from "../../../utils/Constants";
import { ConfirmationPopup } from "../../ConfirmationPopup";
import { ContractCard } from "../contract_card/ContractCard";

export const PendingContracts = () => {
  const [isAcceptContractConfirmationVisible, setIsAcceptContractConfirmationVisible] = useState(false);
  const [isRejectContractConfirmationVisible, setIsRejectContractConfirmationVisible] = useState(false);
  const [selectedContract, setSelectedContract] = useState(null);
  const [contracts, setContracts] = useState([]);
  const { getRequest } = useAxios();

  useEffect(() => {
    // [TODO]: Uncomment after adding API
    // loadContracts()
  }, []);

  const loadContracts = async () => {
    try {
      const response = await getRequest(ENDPOINTS.GET_CONTRACTS);
      setContracts(response.data.data.contracts);
    } catch (error) {
      console.log("Failed to load contracts", error);
    }
  }

  const handleAcceptContract = (shouldShow, contract) => {
    setIsAcceptContractConfirmationVisible(shouldShow);
    setSelectedContract(contract)
  }

  const handleRejectContract = (shouldShow, contract) => {
    setIsRejectContractConfirmationVisible(shouldShow);
    setSelectedContract(contract)
  }

  return (
    <Container className="base-container pt-4 pb-4 d-flex justify-content-start">
      <Stack direction="horizontal">
        {
          contracts.length > 0 ? (
            contracts.map(contract => (
              <ContractCard
                contract={contract}
                showAcceptDialog={() => setIsAcceptContractConfirmationVisible(true)}
                showRejectDialog={() => setIsRejectContractConfirmationVisible(true)}
                onSelectContract={(contract) => setSelectedContract(contract)}
                key={contract.id}
              />
            ))
          ) : (
            <Container fluid className="empty-contracts-view d-flex align-items-center justify-content-center">
              <div>
                <Stack className="align-items-center" gap={3}>
                  <img src={EmptyListView} alt="NavigateLeft" width="200px" height="200px" />
                  <div className="empty-contracts-text">No pending contracts available</div>
                </Stack>
              </div>
            </Container>
          )
        }
      </Stack>

      {isAcceptContractConfirmationVisible && (
        <ConfirmationPopup
          message="Are you sure you accept the contract?"
          onConfirm={() => {
            console.log(`Accepted the contract: ${selectedContract.serviceName}`)
            handleAcceptContract(false, null)
          }}
          onCancel={() => handleAcceptContract(false, null)}
        />
      )}

      {isRejectContractConfirmationVisible && (
        <ConfirmationPopup
          message="Are you sure you reject the contract?"
          onConfirm={() => {
            console.log(`Rejected the contract: ${selectedContract.serviceName}`)
            handleRejectContract(false, null)
          }}
          onCancel={() => handleRejectContract(false, null)}
        />
      )}
    </Container>
  );
}