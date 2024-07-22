import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { AppRoutes } from "./AppRoutes";

export const AuthenticatedRoutes = () => {
  const { isUserLoggedIn } = useAuth();
  return (isUserLoggedIn ? <Outlet /> : <Navigate to={AppRoutes.Landing} />);
};

export const NonAuthenticatedRoutes = () => {
  const { isUserLoggedIn } = useAuth();
  return (isUserLoggedIn ? <Navigate to={AppRoutes.Dashboard} /> : <Outlet />);
}
