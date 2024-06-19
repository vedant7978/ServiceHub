import React from 'react';
import { Toast, ToastContainer } from 'react-bootstrap';
import "./AppToast.css"

const AppToast = ({ show, setShow, title, message }) => {

  return (
    <ToastContainer position="top-end" className="p-3">
      <Toast onClose={() => setShow(false)} show={show} delay={10000} autohide>
        <Toast.Header>
          <div className="mr-auto">
            <strong>{title}</strong>
          </div>
        </Toast.Header>
        <Toast.Body>{message}</Toast.Body>
      </Toast>
    </ToastContainer>
  );
};

export default AppToast;
