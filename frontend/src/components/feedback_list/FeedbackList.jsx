import React from 'react';
import './FeedbackList.css';
import FeedbackCard from "../feedback_card/FeedbackCard";

const FeedbackList = ({ feedbacks }) => {
  return (
    <div className="card p-3">
      <h3 className="mb-3">Feedbacks</h3>

      <div className="feedback-container">

        {feedbacks.map((feedback, index) => (<>
            <FeedbackCard key={index} {...feedback} />
          </>
        ))}
      </div>
    </div>
  );
};

export default FeedbackList;
