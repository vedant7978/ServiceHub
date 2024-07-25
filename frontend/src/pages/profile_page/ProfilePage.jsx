import { HttpStatusCode } from "axios";
import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './ProfilePage.css';
import { Button, Modal } from "react-bootstrap";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import AppToast from "../../components/app_toast/AppToast";
import FeedbackList from "../../components/feedback_list/FeedbackList";
import Profile from "../../components/profile/Profile";
import { useAxios } from '../../context/AxiosContext';
import { ENDPOINTS } from '../../utils/Constants';

const ProfilePage = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [showPasswordModal, setShowPasswordModal] = useState(false);
  const [showToast, setShowToast] = useState(false);
  const [toastTitle, setToastTitle] = useState("");
  const [toastMessage, setToastMessage] = useState("");
  const { getRequest, putRequest } = useAxios();

  const [showPassword, setShowPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const toggleShowPassword = () => setShowPassword(!showPassword);
  const toggleShowNewPassword = () => setShowNewPassword(!showNewPassword);
  const toggleShowConfirmPassword = () => setShowConfirmPassword(!showConfirmPassword);

  const openPasswordModal = () => setShowPasswordModal(true);
  const closePasswordModal = () => {
    setOldPassword('')
    setNewPassword('')
    setConfirmPassword('')
    setShowPasswordModal(false);
  }

  useEffect(() => {
    getUserFeedbacks();
  }, []);

  const getUserFeedbacks = async () => {
    try {
      const response = await getRequest(ENDPOINTS.GET_FEEDBACK, true);
      const data = response.data.data.feedbacks;

      if (response.status === 200) {
        setFeedbacks(data);
      }
    } catch (error) {
      console.error("Failed to update profile:", error);
    }
  };

  const handleChangePassword = async () => {
    if (!oldPassword.trim() || !newPassword.trim() || newPassword.length < 8 || newPassword !== confirmPassword) {
      setToastTitle("Error")
      setToastMessage("Password must not be empty and should have at least 8 characters")
      setShowToast(true)
      return;
    }
    try {
      const data = { oldPassword: oldPassword, newPassword: newPassword }
      const response = await putRequest(ENDPOINTS.NEW_PASSWORD, true, data);
      if (response.status === HttpStatusCode.Ok) {
        setToastTitle("Success")
        setToastMessage("Password updated successfully")
      } else {
        setToastTitle("Error")
        setToastMessage(response.data.message)
      }
      setShowToast(true)
      closePasswordModal()
    } catch (error) {
      setToastTitle("Error")
      setToastMessage(error.response.data.message)
      setShowToast(true)
      console.error("Failed to update password:", error.response.data);
    }
  };

  return (
    <div className="profile-page">
      <div className="container-fluid profile-page-container mt-5">
        <div className="row">
          <div className="col-md-6">
            <Profile onChangePasswordClicked={openPasswordModal} />
          </div>
          <div className="col-md-6">
            <FeedbackList feedbacks={feedbacks} />
          </div>
        </div>
      </div>

      <Modal show={showPasswordModal} onHide={closePasswordModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Change Password</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="mb-3">
            <label className="form-label">Old Password</label>
            <div className="input-group">
              <input type={showPassword ? "text" : "password"} className="form-control" value={oldPassword} onChange={(e) => setOldPassword(e.target.value)} />
              <button className="btn btn-outline-secondary" onClick={toggleShowPassword}>
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>
          </div>
          <div className="mb-3">
            <label className="form-label">New Password</label>
            <div className="input-group">
              <input type={showNewPassword ? "text" : "password"} className="form-control" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
              <button className="btn btn-outline-secondary" onClick={toggleShowNewPassword}>
                {showNewPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>
          </div>
          <div className="mb-3">
            <label className="form-label">Re-enter New Password</label>
            <div className="input-group">
              <input type={showConfirmPassword ? "text" : "password"} className="form-control" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
              <button className="btn btn-outline-secondary" onClick={toggleShowConfirmPassword}>
                {showConfirmPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={closePasswordModal}>
            Close
          </Button>
          <Button variant="primary" onClick={handleChangePassword}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
      <AppToast show={showToast} setShow={setShowToast} title={toastTitle} message={toastMessage}/>
    </div>
  );
};

export default ProfilePage;
