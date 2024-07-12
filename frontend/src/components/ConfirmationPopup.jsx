import React from 'react';
import { Button } from 'react-bootstrap';

export const ConfirmationPopup = ({ message, onConfirm, onCancel }) => {
  return (
    <div className="d-flex justify-content-center align-items-center position-fixed top-0 start-0 w-100 h-100 bg-dark bg-opacity-50" style={{ zIndex: 1050 }}>
      <div className="bg-white p-4 rounded shadow-lg">
        <p className="text-dark mb-4">{message}</p>
        <div className="d-flex justify-content-center gap-2">
          <Button variant="success" onClick={onConfirm}>
            Confirm
          </Button>
          <Button variant="danger" onClick={onCancel}>
            Cancel
          </Button>
        </div>
      </div>
    </div>
  );
};
