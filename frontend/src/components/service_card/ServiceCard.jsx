import React, { useEffect, useState } from 'react';
import { Card } from 'react-bootstrap';
import { FaDollarSign, FaStar } from 'react-icons/fa';
import default_profile_pic from "../../assets/default_profile_pic.png";
import './ServiceCard.css';

export default function ServiceCard({ service, onClick }) {
  const roundedRating = service.averageRating ? service.averageRating.toFixed(1) : '0.0';
  const [imageUrl, setImageUrl] = useState(default_profile_pic);

  useEffect(() => {
    if (service !== null && service.providerImage)
      setImageUrl(service.providerImage)
  }, [service]);

  return (
    <Card className="mb-3 service-card">
      <Card.Body>
        <div className="card-flex-container">
          <div className="d-flex">
            <a href="#" onClick={() => onClick(service)} className='me-3'>
              <Card.Img variant="top" src={imageUrl} className="service-image" onError={() => setImageUrl(default_profile_pic)} />
            </a>
            <div className='d-flex flex-row justify-content-between align-items-center w-100'>

              <div className='d-flex flex-column'>

                <a href="#" onClick={() => onClick(service)} className="card-title-link">
                  {service.name}
                </a>

              </div>
            </div>
          </div>
          <div className="content-container">
            <Card.Text className="card-text-custom">
            <b>Type: </b>{service.type}
            </Card.Text>
            <Card.Text>
              {service.description}
            </Card.Text>
            <div className="details-container">
              <div className="detail-item">
                <FaDollarSign className="icon" />
                <b>{service.perHourRate}/hour</b>
                <b><span style={{ marginLeft: '16px' }}>{roundedRating}</span></b>
                <FaStar className="icon" style={{ color: 'gold' }} />
              </div>
            </div>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}
