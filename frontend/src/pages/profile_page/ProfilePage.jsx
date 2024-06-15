import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './ProfilePage.css';
import FeedbackList from "../../components/feedback_list/FeedbackList";
import Profile from "../../components/profile/Profile";
import { useAxios } from '../../context/AxiosContext';
import { ENDPOINTS } from '../../utils/Constants';

const ProfilePage = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const { getRequest } = useAxios();

  useEffect(() => {
    getUserFeedbacks();
  }, []);

  const getUserFeedbacks = async () => {
    try {
      const userData = { userId: 10 }
      const response = await getRequest(ENDPOINTS.GET_FEEDBACK, true, userData);
      const data = response.data.data.feedbacks;

      if (response.status === 200) {
        setFeedbacks(data);
      }
    } catch (error) {
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
