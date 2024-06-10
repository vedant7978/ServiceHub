import React from 'react';
import './Profile.css';

const Profile = () => {
  return (
    <div className="profile">
      <div className="profile-image">
        <div className="image-placeholder"></div>
        <button className="upload-button">
          <i className="icon-upload"></i>
        </button>
      </div>
      <div className="profile-details">
        <div className="form-group">
          <label>Name</label>
          <input type="text" />
        </div>
        <div className="form-group">
          <label>Email Address</label>
          <input type="email" />
        </div>
        <div className="form-group">
          <label>Phone number</label>
          <input type="tel" />
        </div>
        <div className="form-group">
          <label>Address</label>
          <textarea />
        </div>
        <div className="profile-actions">
          <button className="change-password">Change Password</button>
          <button className="save">Save</button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
