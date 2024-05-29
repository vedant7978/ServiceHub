import React from 'react';
import { Container, Row, Col, Form, Button, Image } from 'react-bootstrap';
import './LoginPage.css';
import loginPageImg from '../../assets/loginPage.jpg'; 

const Login = () => {
  return (
    <Container fluid className="login-page d-flex align-items-center justify-content-center">
      <Row className="login-box p-4">
        <Col md={6} lg={6} className="p-4">
          <div className="text-left mb-4">
            <h1 className="h4 mt-3">Log In</h1>
          </div>
          <Form>
            <Form.Group controlId="formBasicUsername">
              <Form.Label>Email Id</Form.Label>
              <Form.Control type="text" placeholder="Enter Email Id" />
            </Form.Group>

            <Form.Group controlId="formBasicPassword" className="mt-3">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" placeholder="Enter Password" />
            </Form.Group>

            <Form.Group className="text-end mt-2">
              <Form.Text>
                <a href="#" className="text-muted">Forgot Password?</a>
              </Form.Text>
            </Form.Group>

            <Button variant="primary" type="submit" className="w-100 mt-3">
              Log In
            </Button>
             <Form.Group className="text-center mt-2">
              <Form.Text>
                <a href="#" className="text-muted">Don't have an account? Sign Up</a>
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
}

export default Login;
