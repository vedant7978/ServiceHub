import React from 'react';
import './FeedbackList.css';
import FeedbackCard from "../feedback_card/FeedbackCard"; // Additional custom styles if needed

const FeedbackList = ({ feedbacks }) => {
  return (
    <div>
      <h3 className="mb-3">Feedbacks</h3>
      {feedbacks.map((feedback, index) => (
        <FeedbackCard key={index} {...feedback} />
      ))}
    </div>
  );
};

export default FeedbackList;
