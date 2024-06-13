import React from 'react';
import './Profile.css'; // Additional custom styles if needed

const Profile = () => {
  return (
    <div className="card p-3">
      <div className="d-flex justify-content-center mb-3">
        <div className="position-relative">
          <div className="image-placeholder bg-primary rounded-circle" style={{ width: "150px", height: "150px" }}></div>
          <button className="btn btn-secondary position-absolute bottom-0 end-0">
            <i className="bi bi-upload"></i>
          </button>
        </div>
      </div>
      <div className="profile-details">
        <div className="mb-3">
          <label className="form-label">Name</label>
          <input type="text" className="form-control" />
        </div>
        <div className="mb-3">
          <label className="form-label">Email Address</label>
          <input type="email" className="form-control" />
        </div>
        <div className="mb-3">
          <label className="form-label">Phone number</label>
          <input type="tel" className="form-control" />
        </div>
        <div className="mb-3">
          <label className="form-label">Address</label>
          <textarea className="form-control"></textarea>
        </div>
        <div className="d-flex justify-content-between">
          <button className="btn btn-link">Change Password</button>
          <button className="btn btn-success">Save</button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
