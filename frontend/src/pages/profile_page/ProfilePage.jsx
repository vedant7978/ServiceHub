import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './ProfilePage.css';
import Profile from "../../components/profile/Profile";
import FeedbackList from "../../components/feedback_list/FeedbackList"; // Additional custom styles if needed

const ProfilePage = () => {
  const feedbacks = [
    { username: "Username", rating: "4.0", description: "Feedback description ..." },
    { username: "Username", rating: "4.0", description: "Feedback description ..." },
  ];

  return (
    <div className="container mt-5">
      <div className="row">
        <div className="col-md-6">
          <Profile />
        </div>
        <div className="col-md-6">
          <FeedbackList feedbacks={feedbacks} />
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
