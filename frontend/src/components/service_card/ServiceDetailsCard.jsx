import React, { useState, useEffect } from 'react';
import { Card, Row, Col, ListGroup, Button, Container, OverlayTrigger, Tooltip, FormControl } from 'react-bootstrap';
import Loader from '../../components/Loader';
import default_profile_pic from '../../assets/default_profile_pic.png';
import { FaPlusCircle, FaTimesCircle, FaPhone, FaEnvelope, FaMapMarkerAlt, FaUser, FaCheckCircle } from 'react-icons/fa';

export default function ServiceDetailsCard({ selectedService, providerLoading, providerInfo, rightIconOnclick, currentPage, wishlistServiceIds }) {
  const [icon, setIcon] = useState(null);
  const [userAddress, setUserAddress] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    if (currentPage === 'dashboard') {
      if (wishlistServiceIds.includes(selectedService?.id)) {
        setIcon(<FaCheckCircle />);
      } else {
        setIcon(<FaPlusCircle />);
      }
    } else if (currentPage === 'wishlist') {
      setIcon(<FaTimesCircle />);
    }
  }, [currentPage, wishlistServiceIds, selectedService?.id]);

  const handleAddressChange = (event) => {
    setUserAddress(event.target.value);
    if (event.target.value.trim() !== '') {
      setError('');
    }
  };

  const handleRequestService = () => {
    if (!userAddress.trim()) {
      setError('Address is required');
      return;
    }
    console.log('Requesting service with address:', userAddress);
    setError('');
  };

  return (
    <Card style={{ boxShadow: '0 4px 8px rgba(0, 0, 0, 0.3)', marginTop: "12px", maxHeight: '69vh', minHeight: "69vh" }}>
      {providerLoading ? (
        <Container fluid className='w-100'><Loader /></Container>
      ) : (
        <>
          <Card.Body>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
              <Row className='w-100'>
                <Col md={2} className="d-flex justify-content-center">
                  <Card.Img variant="top" src={default_profile_pic} className="service-image" />
                </Col>
                <Col className="d-flex flex-column justify-content-center">
                  <Card.Title>{selectedService?.name}</Card.Title>
                </Col>
              </Row>
              <OverlayTrigger
                placement="top"
                overlay={<Tooltip>{currentPage === 'dashboard' ? 'Add to wishlist' : 'Remove from wishlist'}</Tooltip>}
              >
                <Button
                  variant="light"
                  onClick={() => rightIconOnclick(selectedService.id)}
                >
                  {icon}
                </Button>
              </OverlayTrigger>
            </div>
            <Card.Text><FaUser style={{ marginRight: '8px' }} /> {providerInfo?.name}</Card.Text>
            <Card.Text><FaPhone style={{ marginRight: '8px' }} /> {providerInfo?.phone}</Card.Text>
            <Card.Text><FaEnvelope style={{ marginRight: '8px' }} /> {providerInfo?.email}</Card.Text>
            <div className='d-flex'>
              <Card.Text><FaMapMarkerAlt style={{ marginRight: '8px' }} />
              </Card.Text>
              <FormControl
                type="text"
                placeholder="Enter your address"
                value={userAddress}
                onChange={handleAddressChange}
                className="mb-3"
                style={{ width: '300px' }}
              />
            </div>
            <Card.Text>
              <b>Reviews and Ratings </b>
            </Card.Text>
            <div style={{ maxHeight: '120px', overflowY: 'auto', scrollbarWidth: 'thin' }}>
              <ListGroup variant="flush">
                {selectedService?.feedbacks?.length ? (
                  selectedService.feedbacks.map((feedback, idx) => (
                    <ListGroup.Item key={idx}>
                      <strong>{feedback.username}</strong><b>{feedback.rating}/5.0 </b><br />
                      {feedback.description}
                    </ListGroup.Item>
                  ))
                ) : (
                  <ListGroup.Item>No feedbacks available</ListGroup.Item>
                )}
              </ListGroup>
            </div>
          </Card.Body>
          <Card.Footer style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', borderTop: 'none', marginBottom: '15px', backgroundColor:'white'}}>
            <Button variant="danger" onClick={handleRequestService}>Request Service</Button>
          </Card.Footer>
        </>
      )}
    </Card>
  );
}
