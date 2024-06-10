import React from 'react';
import './ProfilePage.css';
import FeedbackList from "../../components/feedback_list/FeedbackList";
import Profile from "../../components/profile/Profile";
import {Container} from "react-bootstrap";

const ProfilePage = () => {
  const feedbacks = [
    { username: "Username", rating: "4.0", description: "Feedback description ..." },
    { username: "Username", rating: "4.0", description: "Feedback description ..." },
  ];

  return (
    <Container fluid className="profile">
      <Profile />
      <FeedbackList feedbacks={feedbacks} />
    </Container>
  );
};

export default ProfilePage;
