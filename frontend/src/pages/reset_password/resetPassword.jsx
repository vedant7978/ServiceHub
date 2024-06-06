import React, {useState} from "react";
import {Button, Container, Form} from "react-bootstrap";
import {Link} from "react-router-dom";
import {LOGIN, REGISTER} from "../../utils/Routes";
import './resetPassword.css'

const ResetPassword = () => {

    const [formData, setFormData] = useState({
        password: "",
        confirmPassword: ""
    });
    const [error, setError] = useState("");

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
        setError("");
    };

    const handleResetPassword = () => {
        const { password, confirmPassword } = formData;

        if (password !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        if (password.length < 8) {
            setError("Password must be at least 8 characters");
            return;
        }

        setError("");
        // Proceed with resetting password
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
                                name="confirmpassword"
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
                                <Link to={LOGIN} className="text-muted" >Back to login</Link>
                            </Form.Text>
                        </Form.Group>
                    </Form>
                </div>

            </Container>
        </div>
    );
};

export default ResetPassword;