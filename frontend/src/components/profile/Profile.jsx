import React, { useEffect, useState } from 'react';
import { Button, Modal } from 'react-bootstrap';
import './Profile.css';
import { FaCamera } from 'react-icons/fa';
import { useAuth } from "../../context/AuthContext";
import { useAxios } from "../../context/AxiosContext";
import { ENDPOINTS } from '../../utils/Constants';

const Profile = () => {
  const { loggedInUserEmail } = useAuth();
  const [name, setName] = useState('');
  const [email, setEmail] = useState(loggedInUserEmail);
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');
  const [image, setImage] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [error, setError] = useState('');
  const { getRequest, putRequest } = useAxios();

  useEffect(() => {
    getUserData();
  }, []);

  const getUserData = async () => {
    try {
      const userData = { email: email };
      const response = await getRequest(ENDPOINTS.GET_USER_DATA, true, userData);
      const data = response.data;

      if (response.status === 200) {
        setName(data.name);
        setEmail(data.email);
        setAddress(data.address);
        setPhone(data.phone);
        setImage(data.image);
      } else {
        setError('Failed to update profile');
      }
    } catch (error) {
      setError('Failed to update profile');
      console.error("Failed to update profile:", error);
    }
  };

  const handleNameChange = (e) => setName(e.target.value);
  const handlePhoneChange = (e) => setPhone(e.target.value);
  const handleAddressChange = (e) => setAddress(e.target.value);

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      // handle image upload
      const reader = new FileReader();
      reader.onloadend = () => {
        setImage(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const validate = () => {
    let errors = {};
    if (!name.trim()) errors.name = "Name is required";

    if (!/^\d{3}-\d{3}-\d{4}$/.test(phone)) {
      errors.phone = "Phone number must be in the format XXX-XXX-XXXX";
    }
    if (!address.trim()) errors.address = "Address is required";
    return errors;
  };

  const handleSave = async () => {
    const errors = validate();
    if (Object.keys(errors).length > 0) {
      setError(errors);
      return;
    }

    const userData = { name, email, phone, address, image };

    try {
      const response = await putRequest(ENDPOINTS.UPDATE_INTO_PROFILE, true, userData);
      if (response.status === 200) {
        setSuccessMessage('Profile updated successfully');
      } else {
        setError('Failed to update profile');
      }
    } catch (error) {
      setError('Failed to update profile');
      console.error("Failed to update profile:", error);
    }
  };

  const handleChangePassword = () => {
    // TODO: handle change password
  };

  const openModal = () => setShowModal(true);
  const closeModal = () => setShowModal(false);

  return (
    <div className="card p-3">
      <div className="d-flex mb-3">
        <div className="position-relative profile-image-container">
          <img src={image || "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"} alt="Profile" className="profile-image" />
          <div className="upload-icon" onClick={openModal}>
            <FaCamera />
          </div>
        </div>
      </div>
      <div className="profile-details">
        <div className="mb-3">
          <label className="form-label">Name</label>
          <input type="text" className="form-control" value={name} onChange={handleNameChange} />
        </div>
        <div className="mb-3">
          <label className="form-label">Email Address</label>
          <input type="email" className="form-control read-only-email" value={email} readOnly />
        </div>
        <div className="mb-3">
          <label className="form-label">Phone number</label>
          <input type="tel" className="form-control" value={phone} onChange={handlePhoneChange} />
        </div>
        <div className="mb-3">
          <label className="form-label">Address</label>
          <textarea className="form-control" value={address} onChange={handleAddressChange}></textarea>
        </div>
        <div className="d-flex justify-content-between">
          <button className="btn btn-link" onClick={handleChangePassword}>Change Password</button>
          <button className="btn btn-success" onClick={handleSave}>Save</button>
        </div>
        {error && <div className="alert alert-danger mt-3">{error}</div>}
        {successMessage && <div className="alert alert-success mt-3">{successMessage}</div>}
      </div>

      <Modal show={showModal} onHide={closeModal}>
        <Modal.Header closeButton>
          <Modal.Title>Upload Image</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <input type="file" accept="image/*" onChange={handleImageChange} />
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={closeModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default Profile;
