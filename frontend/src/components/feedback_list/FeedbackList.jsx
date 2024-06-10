import React from 'react';
import './FeedbackList.css';
import FeedbackCard from "../feedback_card/FeedbackCard";

const FeedbackList = ({ feedbacks }) => {
  return (
    <div className="feedback-list">
      <h3>Feedbacks</h3>
      {feedbacks.map((feedback, index) => (
        <FeedbackCard key={index} {...feedback} />
      ))}
    </div>
  );
};

export default FeedbackList;
