import React, { useEffect, useState } from 'react';
import { Button, Image, Modal, Stack } from 'react-bootstrap';
import './UserModal.css';
import ImagePlaceholder from "../../assets/ProfileImagePlaceholder.png";

export const UserModal = ({ show, handleClose, user }) => {
  const [imageUrl, setImageUrl] = useState(ImagePlaceholder);

  useEffect(() => {
    if (user !== null && user.image)
      setImageUrl(user.image)
  }, [user]);

  return (
    <Modal show={show} onHide={handleClose} centered size="lg" className="user-modal">
      <Modal.Header closeButton className="user-modal-header">
        <Modal.Title className="user-modal-title">User Details</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="text-center mb-4">
          <Image
            src={imageUrl}
            onError={() => setImageUrl(ImagePlaceholder)}
            roundedCircle
            className="profile-pic mb-3"
          />
          <h3 className="user-name">{user.name}</h3>
          <p className="text-muted">{user.email}</p>
        </div>
        <div>
          <Stack direction="horizontal" gap={3} className="d-flex justify-content-center align-items-center">
            <strong>Phone:</strong>
            <p className="m-0">{user.phone}</p>
          </Stack>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handleClose} className="close-btn">
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
};
