import React, { useEffect, useState } from 'react';
import { Button, Modal } from 'react-bootstrap';
import './Profile.css';
import { FaCamera } from 'react-icons/fa';
import { useAxios } from "../../context/AxiosContext";
import { ENDPOINTS } from '../../utils/Constants';

const Profile = ({ onChangePasswordClicked }) => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
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
      const response = await getRequest(ENDPOINTS.GET_USER_DATA, true);
      const user = response.data.data;

      if (response.status === 200) {
        setName(user.name);
        setEmail(user.email);
        setAddress(user.address);
        setPhone(user.phone);
        setImage(user.image);
      } else {
        setError('Failed to update profile');
      }
    } catch (error) {
      setError('Failed to update profile');
      console.error("Failed to update profile:", error);
    }
  };

  const handlePhoneChange = (e) => {
    const phone = e.target.value.replace(/[^\d]/g, "").slice(0, 10);
    let formattedPhone = "";
    if (phone.length > 3) {
      formattedPhone += phone.slice(0, 3) + "-";
      if (phone.length > 6) {
        formattedPhone += phone.slice(3, 6) + "-";
        formattedPhone += phone.slice(6);
      } else {
        formattedPhone += phone.slice(3);
      }
    } else {
      formattedPhone = phone;
    }
    setPhone(formattedPhone);
  }
  const handleNameChange = (e) => setName(e.target.value);
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
    let error = null;
    if (!name.trim())
      error = "Name is required";
    if (!/^\d{3}-\d{3}-\d{4}$/.test(phone))
      error = "Phone number must be in the format XXX-XXX-XXXX";
    if (!address.trim())
      error = "Address is required";
    return error;
  };

  const handleSave = async () => {
    const error = validate();
    if (error) {
      setError(error);
      return;
    }
    setError('')
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
          <button className="btn btn-link" onClick={onChangePasswordClicked}>Change Password</button>
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
