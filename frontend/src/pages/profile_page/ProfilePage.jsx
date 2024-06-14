import React, {useEffect, useState} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './ProfilePage.css';
import Profile from "../../components/profile/Profile";
import FeedbackList from "../../components/feedback_list/FeedbackList";
import {getFeedbacks} from "../../api_service/FeedbackModule";

const ProfilePage = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [successMessage, setSuccessMessage] = useState('');
  const [error, setError] = useState('');
  useEffect( () => {
    getUserFeedbacks();
  }, []);


  const getUserFeedbacks = async () => {
    try {
      const userData = {userId: 10}
      const response = await getFeedbacks(userData);
      const data = response.data.data.feedbacks;

      if (response.status === 200) {
        console.log(data);
        setFeedbacks(data);
        setSuccessMessage('Profile updated successfully');
      } else {
        setError('Failed to update profile');
      }
    } catch (error) {
      setError('Failed to update profile');
      console.error("Failed to update profile:", error);
    }
  };


  return (
    <div className="profile-page">
    <div className="container-fluid mt-5">
      <div className="row">
        <div className="col-md-6">
          <Profile />
        </div>
        <div className="col-md-6">
          <FeedbackList feedbacks={feedbacks} />
        </div>
      </div>
    </div>
    </div>
  );
};

export default ProfilePage;
