import { lazy, Suspense } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Loader from "./components/Loader";
import { AppRoutes } from "./utils/AppRoutes";
import { WithNavBar } from "./utils/WithNavBar";
import { AuthProvider } from "./context/AuthContext";
import { AxiosProvider } from "./context/AxiosContext";

// Keep adding all screens here for lazy loading
const LandingPage = lazy(() => import("./pages/landing_page/LandingPage"));
const Login = lazy(() => import("./pages/login/Login"));
const Register = lazy(() => import("./pages/register/Register"));
const ForgotPassword = lazy(() => import("./pages/forgotpassword_screen/ForgotPassword"));
const ResetPassword = lazy(() => import("./pages/reset_password/resetPassword"));
const Dashboard = lazy(() => import("./pages/dashboard/Dashboard"))
const ManageServices = lazy(() => import("./pages/manage_services/ManageServices"))
const ManageContracts = lazy(() => import("./pages/manage_contracts/ManageContracts"))
const Wishlist = lazy(() => import("./pages/wishlist/Wishlist"))
const Profile = lazy(() => import("./pages/profile_page/ProfilePage"))

export default function App() {
  return (
    <AuthProvider>
      <AxiosProvider>
        <BrowserRouter>
          <Routes>
            <Route path={AppRoutes.Landing} element={withoutNavBar(LandingPage)} />
            <Route path={AppRoutes.Login} element={withoutNavBar(Login)} />
            <Route path={AppRoutes.Register} element={withoutNavBar(Register)} />
            <Route path={AppRoutes.ForgotPassword} element={withoutNavBar(ForgotPassword)} />
            <Route path={AppRoutes.ResetPassword} element={withoutNavBar(ResetPassword)} />
            <Route path={AppRoutes.Dashboard} element={withNavBar(Dashboard)} />
            <Route path={AppRoutes.ManageServices} element={withNavBar(ManageServices)} />
            <Route path={AppRoutes.ManageContracts} element={withNavBar(ManageContracts)} />
            <Route path={AppRoutes.Wishlist} element={withNavBar(Wishlist)} />
            <Route path={AppRoutes.Profile} element={withNavBar(Profile)} />
          </Routes>
        </BrowserRouter>
      </AxiosProvider>
    </AuthProvider>
  );
}

const withNavBar = (Component) => (
  <WithNavBar>
    <Suspense fallback={<Loader />}>
      <Component />
    </Suspense>
  </WithNavBar>
);

const withoutNavBar = (Component) => (
  <Suspense fallback={<Loader />}>
    <Component />
  </Suspense>
);
