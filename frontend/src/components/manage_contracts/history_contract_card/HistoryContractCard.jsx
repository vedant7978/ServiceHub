import React, { useState } from "react";
import { Stack } from "react-bootstrap";
import "./HistoryContractCard.css";
import ImagePlaceholder from "../../../assets/ProfileImagePlaceholder.png";

export const HistoryContractCard = ({ contract, isSelected, onSelect, onViewProfileClicked }) => {
  const [imageUrl, setImageUrl] = useState(ImagePlaceholder);

  return (
    <div
      className="d-flex history-contract-card p-3 mt-2 mb-2"
      style={{ border: isSelected ? "1px solid #0C0059" : "none" }}
      onClick={() => onSelect(contract.id)}
    >
      <Stack direction="horizontal" className="d-flex w-100 align-items-start flex-md-row flex-column" gap={3}>
        <img
          src={imageUrl}
          onError={() => setImageUrl(ImagePlaceholder)}
          className="contract-image"
          alt="User profile image"
        />
        <div className="h-100 w-100 d-flex">
          <Stack className="justify-content-between align-items-start">
            <div className="w-100">
              <Stack direction="vertical" gap={1}>
                <div className="contract-name">{contract.serviceName || "No name"}</div>
                <div className="contract-type">{contract.serviceType || "No service type"}</div>
                <div className="contract-description mt-2">{contract.serviceDescription || "No description"}</div>
              </Stack>
            </div>
            <div className="w-100">
              <Stack direction="horizontal" className="w-100 justify-content-between">
                {/*[TODO]: Update logic in another feature i.e. accept/reject contract*/}
                <div><b>Status:</b> Pending</div>
                <div className="view-profile" onClick={() => onViewProfileClicked(contract.serviceProviderId)}>
                  View profile
                </div>
              </Stack>
            </div>
          </Stack>
        </div>
      </Stack>
    </div>
  );
}