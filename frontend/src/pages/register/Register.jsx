import { HttpStatusCode } from "axios";
import React, { useEffect, useState } from "react";
import { Alert, Button, Container, Form, Stack } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import "./Register.css";
import Loader from "../../components/Loader";
import { useAuth } from "../../context/AuthContext";
import { useAxios } from "../../context/AxiosContext";
import { AppRoutes } from "../../utils/AppRoutes";
import { ENDPOINTS } from "../../utils/Constants";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [hasUserClickedRegister, setHasUserClickedRegister] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
    address: "",
    image: "",
  });
  const { postRequest } = useAxios();
  const { isUserLoggedIn, setUserLoggedIn } = useAuth();

  useEffect(() => {
    if (isUserLoggedIn) {
      navigate(AppRoutes.Dashboard);
    }
  }, []);

  const validate = () => {
    let errors = {};
    if (!formData.name.trim()) errors.name = "Name is required";
    if (!formData.email.trim()) {
      errors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = "Email is invalid";
    }
    if (!formData.password.trim()) {
      errors.password = "Password is required";
    } else if (formData.password.length < 8) {
      errors.password = "Password must be at least 8 characters long";
    }
    if (!/^\d{3}-\d{3}-\d{4}$/.test(formData.phone)) {
      errors.phone = "Phone number must be 10 digits";
    }
    if (!formData.address.trim()) errors.address = "Address is required";
    return errors;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    if (hasUserClickedRegister) {
      setErrors({
        ...errors,
        [name]: value.trim() ? "" : capitalize(name) + " is required",
      });
    }
  };

  const handlePhoneChange = (e) => {
    const phone = e.target.value.replace(/[^\d]/g, "").slice(0, 10);
    let formattedPhone = "";
    if (phone.length > 3) {
      formattedPhone += phone.slice(0, 3) + "-";
      if (phone.length > 6) {
        formattedPhone += phone.slice(3, 6) + "-";
        formattedPhone += phone.slice(6);
      } else {
        formattedPhone += phone.slice(3);
      }
    } else {
      formattedPhone = phone;
    }
    setFormData({ ...formData, phone: formattedPhone });
    if (hasUserClickedRegister) {
      setErrors({
        ...errors,
        [e.target.name]: formattedPhone.trim()
          ? ""
          : "Phone number is required",
      });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setHasUserClickedRegister(true);
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      console.error("Error validating: ", validationErrors);
      return;
    }

    try {
      setApiError(null);
      setLoading(true);
      const response = await postRequest(ENDPOINTS.REGISTER, false, formData);
      const result = response.data;
      const message = result.message;

      if (response.status === HttpStatusCode.Created) {
        setUserLoggedIn(result.token);
        navigate(AppRoutes.Dashboard);
      } else {
        setApiError(
          message === null
            ? "Unexpected response from server."
            : "Failed to register user: " + message
        );
      }
    } catch (error) {
      console.error("Registration failed:", error);
      setApiError(
        error.response?.data || "Registration failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  const capitalize = (str) => {
    return str.charAt(0).toUpperCase() + str.slice(1);
  };

  return (
    <Container fluid className="registration-page py-5">
      <Stack
        direction="vertical"
        gap={3}
        className="registration-box my-auto justify-content-between py-6"
      >
        <div>
          <Stack gap={3}>
            <div className="app-information-title">Register</div>
          </Stack>
        </div>

        {apiError && <Alert variant="danger">{apiError}</Alert>}

        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="formName" className="mb-3">
            <Form.Label>Name</Form.Label>
            <Form.Control
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              isInvalid={!!errors.name}
            />
            <Form.Control.Feedback type="invalid">
              {errors.name}
            </Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formEmail" className="mb-3">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              isInvalid={!!errors.email}
            />
            <Form.Control.Feedback type="invalid">
              {errors.email}
            </Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formPassword" className="mb-3">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              isInvalid={!!errors.password}
            />
            <Form.Control.Feedback type="invalid">
              {errors.password}
            </Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formPhone" className="mb-3">
            <Form.Label>Phone</Form.Label>
            <Form.Control
              type="text"
              name="phone"
              value={formData.phone}
              onChange={handlePhoneChange}
              isInvalid={!!errors.phone}
            />
            <Form.Control.Feedback type="invalid">
              {errors.phone}
            </Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formAddress" className="mb-3">
            <Form.Label>Address</Form.Label>
            <Form.Control
              type="text"
              name="address"
              value={formData.address}
              onChange={handleChange}
              isInvalid={!!errors.address}
            />
            <Form.Control.Feedback type="invalid">
              {errors.address}
            </Form.Control.Feedback>
          </Form.Group>

          {loading && <Loader />}

          <Button
            type="submit"
            className="colored-primary mt-3"
            style={{ width: "100%" }}
            disabled={loading}
          >
            {loading ? "Registering..." : "Register"}
          </Button>
        </Form>

        <Form.Group className="text-center mt-2">
          <Form.Text>
            <span
              className="login-text"
              style={{ cursor: "pointer" }}
              onClick={() => navigate("/login")}
            >
              Already have an account? Log in
            </span>
          </Form.Text>
        </Form.Group>
      </Stack>
    </Container>
  );
}
