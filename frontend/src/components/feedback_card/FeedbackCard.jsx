import React from 'react';
import './FeedbackCard.css';

const FeedbackCard = ({ username, rating, description }) => {
  return (
    <div className="feedback-card">
      <div className="feedback-header">
        <span className="username">{username}</span>
        <span className="rating">{rating}/5.0</span>
      </div>
      <div className="feedback-description">
        {description}
      </div>
    </div>
  );
};

export default FeedbackCard;
