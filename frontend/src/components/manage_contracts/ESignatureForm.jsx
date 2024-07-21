import React, { useState } from 'react';
import { Button, Form, Modal } from 'react-bootstrap';

export const ESignatureForm = ({ show, onHide, onSubmit, contract }) => {
  const [serviceProvider, setServiceProvider] = useState('');
  const [serviceProviderError, setServiceProviderError] = useState(null);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (serviceProvider !== contract.serviceProviderName) {
      setServiceProviderError("Please enter your name")
      return
    }

    setServiceProviderError(null)
    onSubmit({
      contractId: contract.id,
      serviceName: contract.serviceName,
      serviceRequester: contract.userName,
      serviceCost: contract.perHourRate,
      serviceProvider
    });
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>e-Signature Form</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Service Name</Form.Label>
            <Form.Control
              type="text"
              value={contract.serviceName}
              readOnly
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Service Requester Name</Form.Label>
            <Form.Control
              type="text"
              value={contract.userName}
              readOnly
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Service Cost Per Hour</Form.Label>
            <Form.Control
              type="text"
              value={contract.perHourRate}
              readOnly
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Service Provider Name (e-Signature)</Form.Label>
            <Form.Control
              type="text"
              value={serviceProvider}
              placeholder={contract.serviceProviderName}
              onChange={(e) => setServiceProvider(e.target.value)}
              isInvalid={!!serviceProviderError}
            />
            <Form.Control.Feedback type="invalid">
              {serviceProviderError}
            </Form.Control.Feedback>
          </Form.Group>
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
  );
};
