import React, {useEffect} from 'react';
import {Toast, ToastContainer} from 'react-bootstrap';
import "./AppToast.css"

const AppToast = ({show, setShow, title, message}) => {

  useEffect(() => {
    if (show) {
      setTimeout(() => {
        setShow(false);
      }, 3000);
    }
  }, [show, setShow]);

  return (
    <ToastContainer position="top-end" className="p-3">
      <Toast onClose={() => setShow(false)} show={show} delay={3000} autohide>
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
