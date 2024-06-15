import React from "react";
import { Stack } from "react-bootstrap";
import "./HistoryContractCard.css";
import ImagePlaceholder from "../../../assets/ProfileImagePlaceholder.png";

export const HistoryContractCard = ({ contract, onSelect, onViewProfileClicked }) => {
  return (
    <div
      className="d-flex history-contract-card p-3 mt-2 mb-2"
      style={{ border: contract.isSelected ? "1px solid #0C0059" : "none" }}
      onClick={() => onSelect(contract)}
    >
      <Stack direction="horizontal" className="d-flex w-100 align-items-start" gap={3}>
        <img src={contract.imageUrl || ImagePlaceholder} className="contract-image bg-info" alt="User profile image"/>
        <div className="h-100 w-100 d-flex">
          <Stack className="justify-content-between align-items-start">
            <div>
              <Stack direction="vertical" gap={1}>
                <div className="contract-name">{contract.name || "No name"}</div>
                <div className="contract-type">{contract.type || "No service type"}</div>
                <div className="contract-description mt-2">{contract.description || "No description"}</div>
              </Stack>
            </div>
            <div className="view-profile w-100" onClick={() => onViewProfileClicked(contract.serviceProviderId)}>
              View profile
            </div>
          </Stack>
        </div>
      </Stack>
    </div>
  );
}