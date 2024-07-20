import { HttpStatusCode } from "axios";
import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import "./ForgotPassword.css";
import { Link } from "react-router-dom";
import AppToast from "../../components/app_toast/AppToast";
import { useAxios } from "../../context/AxiosContext";
import { AppRoutes } from "../../utils/AppRoutes";
import { ENDPOINTS } from "../../utils/Constants";
import { validateEmail } from "../../utils/helper";

const ForgotPassword = () => {
  const [email, setEmail] = useState('')
  const [error, setError] = useState('');
  const [showToast, setShowToast] = useState(false);
  const [forgotPasswordSuccessMessage, setForgotPasswordSuccessMessage] = useState("");
  const [isButtonEnabled, setIsButtonEnabled] = useState(true);
  const { postRequest } = useAxios();

  const handleSendMail = async () => {
    if (!validateEmail(email)) {
      setError('Please enter a valid email address');
      return;
    }
    setError('');

    try {
      const userData = {email: email}
      const response = await postRequest(ENDPOINTS.FORGOT_PASSWORD, false, userData);
      const message = response.data.message;

      if (response.status === HttpStatusCode.Ok) {
        setForgotPasswordSuccessMessage(message);
        setShowToast(true);
        setIsButtonEnabled(false);
      } else {
        setError(message);
      }
    } catch (error) {
      setError(error);
      console.error("Failed to send mail:", error);
    }
  }

  const handleEmailChange = (e) => {
    setEmail(e.target.value)
    setError('')
  }

  return (
    <>
      <div className="d-flex justify-content-center align-items-center vh-100 FPMain">
        <Container className="border-right FPContainer">
          <div className="FPformData">
            <h4>Forget Password</h4>
            <p className="FPdescription">
              Enter email address we will send password reset link
            </p>
            <Form className="FPform">
              <Form.Group controlId="formBasicEmail">
                <Form.Label>Email Address</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Please enter your email"
                  value={email}
                  onChange={handleEmailChange}
                />
              </Form.Group>
              <div className="FPerror">{error}</div>
              <Button
                variant="secondary"
                onClick={handleSendMail}
                className="w-100 mt-3"
                disabled={!isButtonEnabled}
              >
                Send email
              </Button>
            </Form>
            <Form>
              <Form.Group className="text-center mt-2">
                <Form.Text>
                  <Link to={AppRoutes.Login} className="text-muted">Back to login</Link>
                </Form.Text>
              </Form.Group>
            </Form>
          </div>
          <AppToast show={showToast} setShow={setShowToast} title={"Email sent successfully"} message={forgotPasswordSuccessMessage}/>

        </Container>

      </div>
    </>
  );
};

export default ForgotPassword;
