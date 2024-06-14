import React from 'react';
import './FeedbackCard.css';

const FeedbackCard = ({ providerName, rating, description }) => {

  return (
    <div className="card mb-3">
      <div className="card-body">
        <div className="d-flex justify-content-between">
          <h5 className="card-title">{providerName}</h5>
          <span className="badge bg-primary">{rating}/5.0</span>
        </div>
        <p className="card-text">{description}</p>
      </div>
    </div>
  );
};

export default FeedbackCard;
