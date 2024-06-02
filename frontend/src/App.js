import { BrowserRouter, Routes, Route } from "react-router-dom";
import { LANDING, LOGIN } from "./utils/Routes";
import { Suspense, lazy } from "react";
import { Spinner } from "react-bootstrap";

// Keep adding all screens here for lazy loading
const LandingPage = lazy(() => import("./pages/landing_page/LandingPage"));
const Login = lazy(() => import("./pages/login/Login"));

export default function App() {
    const Loader = () => (
        <div className="d-flex align-items-center justify-content-center vh-100">
            <Spinner animation="border" variant="primary" size="lg" />
        </div>
    );

    return (
        <BrowserRouter>
            <Suspense fallback={<Loader />}>
                <Routes>
                    <Route path={LANDING} element={<LandingPage />} />
                    <Route path={LOGIN} element={<Login />} />
                </Routes>
            </Suspense>
        </BrowserRouter>
    );
}
