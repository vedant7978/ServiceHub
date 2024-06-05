import { BrowserRouter, Routes, Route } from "react-router-dom";
import { LANDING, LOGIN, REGISTER } from "./utils/Routes";
import { Suspense, lazy } from "react";
import Loader from "./components/Loader";

// Keep adding all screens here for lazy loading
const LandingPage = lazy(() => import("./pages/landing_page/LandingPage"));
const Login = lazy(() => import("./pages/login/Login"));
const Register = lazy(() => import("./pages/register/Register"));

export default function App() {
    return (
        <BrowserRouter>
            <Suspense fallback={<Loader />}>
                <Routes>
                    <Route path={LANDING} element={<LandingPage />} />
                    <Route path={LOGIN} element={<Login />} />
                    <Route path={REGISTER} element={<Register />} />
                </Routes>
            </Suspense>
        </BrowserRouter>
    );
}
