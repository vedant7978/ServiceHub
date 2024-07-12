import React, { useEffect } from "react";
import "./LandingPage.css";
import { Button, Container, Stack } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import AppLogo from "../../assets/AppLogo.png";
import LandingImage from "../../assets/LandingPageImage.png";
import { useAuth } from "../../context/AuthContext";
import { AppRoutes } from "../../utils/AppRoutes";

export default function LandingPage() {
  const navigate = useNavigate();
  const { loggedInUserEmail } = useAuth();

  // [TODO]: Use private routes
  useEffect(() => {
    if (loggedInUserEmail) {
      navigate(AppRoutes.Dashboard);
    }
  }, []);

  return (
    <Container fluid className="landing-page">
      <Stack
        direction="horizontal"
        gap={5}
        className="landing-box my-auto justify-content-center flex-md-row flex-column"
      >
        <Stack
          direction="vertical"
          gap={3}
          className="justify-content-between py-6"
        >
          <div>
            <Stack gap={3}>
              <img src={AppLogo} alt="AppLogo" className="app-logo" />
              <div className="app-information-title">
                Find and Hire Trusted Service Experts Easily
              </div>
              <div className="app-information-description">
                At ServiceHub, we connect you with skilled professionals
                offering a wide range of services, from plumbing and carpentry
                to many more. Browse through our listings, view profiles, and
                hire the right expert at a fair wage. Your perfect service
                provider is just a click away!
              </div>
            </Stack>
          </div>

          <div>
            <Stack gap={3}>
              <Button
                className="outline-primary"
                onClick={() => navigate(AppRoutes.Login)}
              >
                Login
              </Button>
              <Button
                className="colored-primary"
                onClick={() => navigate(AppRoutes.Register)}
              >
                Register
              </Button>
            </Stack>
          </div>
        </Stack>
        <img
          src={LandingImage}
          alt="ServiceHub"
          className="landing-image d-flex align-items-center img-fluid"
        />
      </Stack>
    </Container>
  );
}
