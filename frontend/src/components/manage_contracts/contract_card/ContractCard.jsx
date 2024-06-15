import React from "react";
import { Stack } from "react-bootstrap";
import "./ContractCard.css";
import AcceptIcon from "../../../assets/ContractAcceptIcon.png";
import RejectIcon from "../../../assets/ContractRejectIcon.png";
import ImagePlaceholder from "../../../assets/ProfileImagePlaceholder.png";
import FeedbackCard from "../../feedback_card/FeedbackCard";

export const ContractCard = ({ contract, showAcceptDialog, showRejectDialog, onSelectContract }) => {

  const handleAcceptContract = () => {
    showAcceptDialog();
    onSelectContract(contract)
  }

  const handleRejectContract = () => {
    showRejectDialog()
    onSelectContract(contract)
  }

  return (
    <div className="pending-contract-card p-3">
      <Stack className="h-100 justify-content-between align-items-center">
        <div>
          <Stack className="d-flex" gap={3}>
            <div className="contract-card-title">{contract.serviceName}</div>

            <Stack direction="horizontal" className="align-items-start" gap={3}>
              <img
                className="requester-profile-image"
                src={contract.imageUrl || ImagePlaceholder}
                alt="User profile image"/>
              <Stack>
                <div className="requester-user-name">{contract.userName}</div>
                <div className="requester-rating">{contract.rating}/5.0</div>
              </Stack>
            </Stack>

            <Stack>
              <div className="contract-card-sub-title">Address</div>
              <div className="contract-address">{contract.address}</div>
            </Stack>

            <Stack>
              <div className="contract-card-sub-title">Feedbacks</div>
              <div className="feedback">
                {
                  contract.feedbacks.map((feedback, index) => (
                    <FeedbackCard key={index} feedback={feedback}/>
                  ))
                }
              </div>
            </Stack>
          </Stack>
        </div>
        <div>
          <Stack direction="horizontal" className="mt-3" gap={4}>
            <div className="contract-reject-button d-flex align-items-center justify-content-center">
              <img
                src={RejectIcon}
                alt="Reject contract"
                width="40px"
                height="40px"
                onClick={() => handleRejectContract()}
              />
            </div>
            <div className="contract-accept-button d-flex align-items-center justify-content-center">
              <img
                src={AcceptIcon}
                alt="Accept contract"
                width="40px"
                height="40px"
                onClick={() => handleAcceptContract()}
              />
            </div>
          </Stack>
        </div>
      </Stack>
    </div>
  );
}