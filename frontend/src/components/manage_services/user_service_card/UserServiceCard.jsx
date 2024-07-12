import React, { useEffect, useRef, useState } from "react";
import { OverlayTrigger, Popover, Stack } from "react-bootstrap";
import "./UserServiceCard.css";
import MenuOptionsIcon from "../../../assets/MenuOptionsIcon.png";

export const UserServiceCard = ({ service, isSelected, onEditClicked, onDeleteClicked }) => {

  const [show, setShow] = useState(false);
  const target = useRef(null);

  // Handle popover hide state when clicked outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (target.current && !target.current.contains(event.target)) {
        setTimeout(() => setShow(false), 200);
      }
    };
    if (show) {
      document.addEventListener('mousedown', handleClickOutside);
    } else {
      document.removeEventListener('mousedown', handleClickOutside);
    }
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [show]);

  const popover = (
    <Popover id="popover-basic" className="custom-popover">
      <Popover.Body>
        <div className="popover-item" onClick={onEditClicked}>Edit</div>
        <div className="popover-divider"></div>
        <div className="popover-item" onClick={onDeleteClicked}>Delete</div>
      </Popover.Body>
    </Popover>
  );

  return (
    <div
      className="d-flex service-card p-3 mt-2 mb-2"
      style={{ border: isSelected ? "1px solid #0C0059" : "none" }}
    >
      <Stack direction="horizontal" className="d-flex w-100 align-items-start flex-md-row flex-column" gap={3}>
        <div className="h-100 w-100 d-flex">
          <Stack className="justify-content-between align-items-start">
            <div className="w-100">
              <Stack direction="vertical" gap={1}>
                <Stack direction="horizontal" className="d-flex justify-content-between">
                  <div className="service-name">{service.name || "No name"}</div>
                  <div>
                    <img
                      ref={target}
                      className="service-card-options-image"
                      src={MenuOptionsIcon}
                      width="20px"
                      height="20px"
                      alt="Service card options icon"
                      onClick={() => setShow(!show)}
                    />
                    <OverlayTrigger
                      trigger="click"
                      placement="right"
                      show={show}
                      target={target.current}
                      overlay={popover}
                      rootClose
                      onHide={() => setShow(false)}
                    >
                      <div></div>
                    </OverlayTrigger>
                  </div>
                </Stack>
                <div className="service-type">{service.type || "No service type"}</div>
                <div className="service-description mt-2">{service.description || "No description"}</div>
              </Stack>
            </div>
            <div className="service-per-hour-rate w-100 mt-2 p-1">
              ${service.perHourRate}/hr
            </div>
          </Stack>
        </div>
      </Stack>
    </div>
  );
}