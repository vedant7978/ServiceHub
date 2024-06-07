import React, {useState} from "react";
import {Button, Container, Form} from "react-bootstrap";
import {Link, useLocation, useNavigate} from "react-router-dom";
import {LOGIN} from "../../utils/Routes";
import './resetPassword.css'
import {resetPassword} from "../../api_service/AuthModule";
import HttpStatusCodes from "../../utils/HttpStatusCodes";

const ResetPassword = () => {

    const {state} = useLocation();
    const email = state.email;
    const navigateTo = state.navigateTo;
    const navigate = useNavigate()

    console.log(email);
    const [formData, setFormData] = useState({
        password: "",
        confirmPassword: ""
    });
    const [error, setError] = useState("");

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
            const userData = {email: email, password: formData.password}
            const response = await resetPassword(userData);
            const message = response.data.message;

            if (response.status === HttpStatusCodes.OK) {
               navigate(navigateTo)
            } else {
                setError(message);
            }
        } catch (error) {
            setError(error);
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
                                <Link to={LOGIN} className="text-muted">Back to login</Link>
                            </Form.Text>
                        </Form.Group>
                    </Form>
                </div>

            </Container>
        </div>
    );
};

export default ResetPassword;