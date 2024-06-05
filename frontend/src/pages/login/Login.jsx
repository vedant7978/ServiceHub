import React, { useState } from 'react';
import { Container, Row, Col, Form, Button, Image } from 'react-bootstrap';
import './LoginPage.css';
import { Link, useNavigate } from "react-router-dom";
import loginPageImg from '../../assets/loginPage.jpg';
import { LANDING } from '../../utils/Routes';
import { REGISTER } from '../../utils/Routes';
import { loginUser } from '../../api_service/AuthModule';
import Constants from '../../utils/Constants';
import HttpStatusCodes from '../../utils/HttpStatusCodes';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({});
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const validate = () => {
    let errors = {};
    if (!email.trim()) {
      errors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      errors.email = "Email is invalid";
    }
    if (!password.trim()) {
      errors.password = "Password is required";
    } else if (password.length < 8) {
      errors.password = "Password must be at least 8 characters long";
    }
    return errors;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const validationErrors = validate();
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length === 0) {
      const req = {
        email: email,
        password: password,
      };
      await loginUser(req)
        .then((response) => {
          const token = response.data.token;
          localStorage.setItem(Constants.AUTH_TOKEN_KEY, token);
          // [TODO]: naviagte to home page when it is ready
          navigate(LANDING);
        })
        .catch((error) => {
          if (error.response && error.response.status === HttpStatusCodes.BAD_REQUEST) {
            setError("Invalid login credentials.");
          } else {
            setError("An error occurred during login.");
          }
        });
    }
  };
    
    return (
      <Container fluid className="login-page d-flex align-items-center justify-content-center">
        <Row className="login-box p-4">
          <Col md={6} lg={6} className="p-4">
            <div className="text-left mb-4">
              <h1 className="h4 mt-3">Log In</h1>
            </div>
            <Form onSubmit={handleSubmit}>
              <Form.Group controlId="formBasicEmail">
                <Form.Label>Email Id</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter Email Id"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  isInvalid={!!errors.email}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.email}
                </Form.Control.Feedback>
              </Form.Group>
  
              <Form.Group controlId="formBasicPassword" className="mt-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  isInvalid={!!errors.password}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.password}
                </Form.Control.Feedback>
              </Form.Group>
  
              <Form.Group className="text-end mt-2">
                <Form.Text>
                  <a href="#" className="text-muted">Forgot Password?</a>
                </Form.Text>
              </Form.Group>
  
              {error && (
                <div className="alert alert-danger" role="alert">
                  {error}
                </div>
              )}
  
              <Button variant="primary" type="submit" className="w-100 mt-3">
                Log In
              </Button>
              <Form.Group className="text-center mt-2">
                <Form.Text>
                  <Link to={REGISTER} className="text-muted">Don't have an account? Sign Up</Link>
                </Form.Text>
              </Form.Group>
            </Form>
          </Col>
          <Col md={6} lg={6} className="image-col p-0">
            <Image src={loginPageImg} fluid className="login-image" />
          </Col>
        </Row>
      </Container>
    );
  };

export default Login;
