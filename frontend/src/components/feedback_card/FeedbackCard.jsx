import React from 'react';
import './FeedbackCard.css';
import { Stack } from "react-bootstrap";

const FeedbackCard = ({ feedback }) => {

  return (
    <Stack className="feedback-block p-2 mb-2 mt-2">
      <div>
        <Stack direction="horizontal" className="d-flex justify-content-between">
          <div className="feedback-username">{feedback.providerName}</div>
          <div className="feedback-rating">{feedback.rating.toFixed(2)}/5.00</div>
        </Stack>
      </div>
      <div className="feedback-description">{feedback.description}</div>
    </Stack>
  );
};

export default FeedbackCard;
