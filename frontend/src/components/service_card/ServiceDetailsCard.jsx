import React, { useState, useEffect } from 'react';
import { Card, Row, Col, ListGroup, Button, Container, OverlayTrigger, Tooltip } from 'react-bootstrap';
import Loader from '../../components/Loader';
import default_profile_pic from "../../assets/default_profile_pic.png";
import { FaPlusCircle, FaTimesCircle, FaPhone, FaEnvelope, FaMapMarkerAlt, FaUser} from 'react-icons/fa';
import ServiceCard from './ServiceCard.css';

export default function ServiceDetailsCard({ selectedService, providerLoading, feedbacks, providerInfo, rightIconOnclick, currentPage }) {
  const [icon, setIcon] = useState(null);

  useEffect(() => {
    if (currentPage === 'dashboard') {
      setIcon(<FaPlusCircle />);
    } else if (currentPage === 'wishlist') {
      setIcon(<FaTimesCircle />);
    }
  }, [currentPage]);

  return (
    <Card>
      {providerLoading ? (
        <Container fluid className='w-100'><Loader /></Container>
      ) : (
        <Card.Body>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <Row className='w-100'>
              <Col md={2}>
                <Card.Img variant="top" src={default_profile_pic} className="service-image" />
              </Col>
              <Col>
                <Card.Title>{selectedService.name}</Card.Title>
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
          <Card.Text><FaUser style={{ marginRight: '8px' }}/> {providerInfo.name}</Card.Text>
          <Card.Text><FaPhone style={{ marginRight: '8px' }}/> {providerInfo.phone}</Card.Text>
          <Card.Text><FaEnvelope style={{ marginRight: '8px' }}  />  {providerInfo.email}</Card.Text>
          <Card.Text><FaMapMarkerAlt style={{ marginRight: '8px' }} />{providerInfo.address}</Card.Text>
          <Card.Text>
            <b>Reviews and Ratings </b>
          </Card.Text>
          <ListGroup variant="flush">
            {feedbacks.map((feedback, idx) => (
              <ListGroup.Item key={idx}>
                <strong>{feedback.username}</strong><b>{feedback.rating}/5.0 </b><br />
                {feedback.description}
              </ListGroup.Item>
            ))}
          </ListGroup>
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', padding: '10px' }}>
            <Button variant="danger">Request Service</Button>
          </div>
        </Card.Body>
      )}
    </Card>
  );
}
