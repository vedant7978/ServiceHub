import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import "./ForgotPassword.css";
import { Link } from "react-router-dom";
import { LOGIN } from "../../utils/Routes";
import { validateEmail } from "../../utils/helper";

const ForgotPassword = () => {
    const [email, setEmail] = useState('')
    const [error, setError] = useState('');

    const handleSendMail = ()=>{
        if (!validateEmail(email)) {
            setError('Please enter a valid email address');
            return;
        }
        setError('');
        // Proceed with sending email
    }

    const handleEmailChange = (e)=>{
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
                            >
                                Send email
                            </Button>
                        </Form>
                        <Form>
                            <Form.Group className="text-center mt-2">
                                <Form.Text>
                                    <Link to={LOGIN} className="text-muted" >Back to login</Link>
                                </Form.Text>
                            </Form.Group>
                        </Form>
                    </div>

                </Container>
            </div>
        </>
    );
};

export default ForgotPassword;
