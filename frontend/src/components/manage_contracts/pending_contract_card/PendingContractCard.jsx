import React from "react";
import { Stack } from "react-bootstrap";
import "./PendingContractCard.css";
import ImagePlaceholder from "../../../assets/ProfileImagePlaceholder.png";
import FeedbackCard from "../../feedback_card/FeedbackCard";

export const PendingContractCard = ({ contract, showAcceptDialog, showRejectDialog, onSelectContract }) => {

  const handleAcceptContract = () => {
    showAcceptDialog();
    onSelectContract(contract.id)
  }

  const handleRejectContract = () => {
    showRejectDialog()
    onSelectContract(contract.id)
  }

  return (
    <div className="pending-contract-card p-3">
      <Stack className="h-100 justify-content-between">
        <div>
          <Stack className="d-flex" gap={3}>
            <div className="contract-card-title">{contract.serviceName}</div>

            <Stack direction="horizontal" className="align-items-start" gap={3}>
              <img
                className="requester-profile-image"
                src={contract.imageUrl || ImagePlaceholder}
                alt="User profile image"/>
              <Stack>
                <div className="requester-user-name">Requester: {contract.userName}</div>
                <div className="requester-rating">{contract.rating || "0.0"}/5.0</div>
              </Stack>
            </Stack>

            <Stack>
              <div className="contract-card-sub-title">Address</div>
              <div className="contract-description">{contract.address}</div>
            </Stack>

            <Stack>
              <div className="contract-card-sub-title">Feedbacks</div>
              <div className="feedback">
                {
                  contract.feedbacks.length > 0 ? (
                    contract.feedbacks.map((feedback, index) => (
                      <FeedbackCard key={index} feedback={feedback}/>
                    ))
                  ) : (
                    <div className="contract-description">No feedbacks provided</div>
                  )
                }
              </div>
            </Stack>
          </Stack>
        </div>
        <div>
          <Stack direction="horizontal" className="justify-content-center mt-3" gap={4}>
            <div className="contract-reject-button px-4 py-2" onClick={handleRejectContract}>Reject</div>
            <div className="contract-accept-button px-4 py-2" onClick={handleAcceptContract}>Accept</div>
          </Stack>
        </div>
      </Stack>
    </div>
  );
}