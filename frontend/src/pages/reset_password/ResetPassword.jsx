import { HttpStatusCode } from "axios";
import React, { useEffect, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAxios } from "../../context/AxiosContext";
import { AppRoutes } from "../../utils/AppRoutes";
import { ENDPOINTS } from "../../utils/Constants";
import './ResetPassword.css'

const ResetPassword = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { postRequest } = useAxios();

  // Extract query parameters
  const queryParams = new URLSearchParams(location.search);
  const token = queryParams.get('token');
  const email = queryParams.get('email');

  const [formData, setFormData] = useState({
    password: "",
    confirmPassword: ""
  });
  const [error, setError] = useState("");

  useEffect(() => {
    if (!token || !email)
      navigate(AppRoutes.Landing)
  }, []);

  const handleInputChange = (e) => {
    const {name, value} = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
    setError("");
  };

  const handleResetPassword = async () => {
    const {password, confirmPassword} = formData;

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }
    if (password.length < 8) {
      setError("Password must be at least 8 characters");
      return;
    }

    setError("");
    try {
      const userData = {email: email, password: password, token: token}
      const response = await postRequest(ENDPOINTS.RESET_PASSWORD, false, userData);
      const message = response.data.data.message;

      if (response.status === HttpStatusCode.Ok) {
        navigate(AppRoutes.Landing)
      } else {
        setError(message);
      }
    } catch (error) {
      setError(error.response.data.message);
      console.error("Reset failed:", error);
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100 RpMain">
      <Container className="border-right RpContainer">
        <div className="RpformData">
          <h4>Reset Password</h4>
          <Form className="Rpform">
            <Form.Group controlId="Password">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                name="password"
                placeholder="Please enter your password"
                value={formData.password}
                onChange={handleInputChange}
              />
            </Form.Group>
            <Form.Group controlId="ConfirmPassword" className="RpConfirm">
              <Form.Label>Confirm Password</Form.Label>
              <Form.Control
                type="password"
                name="confirmPassword"
                placeholder="Please enter confirm Password"
                value={formData.confirmPassword}
                onChange={handleInputChange}
              />
            </Form.Group>
            {error && <div className="Rperror">{error}</div>}
            <Button
              variant="secondary"
              onClick={handleResetPassword}
              className="w-100 mt-3"
            >Reset Password
            </Button>
            <Form.Group className="text-center mt-2">
              <Form.Text>
                <Link to={AppRoutes.Login} className="text-muted">Back to login</Link>
              </Form.Text>
            </Form.Group>
          </Form>
        </div>

      </Container>
    </div>
  );
};

export default ResetPassword;