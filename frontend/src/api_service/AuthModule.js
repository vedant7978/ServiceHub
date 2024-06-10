import { ENDPOINTS, postRequest } from "./ApiModule";

// This file includes all API's of the application regarding the AUTH module
// Auth API include /api/auth as prefix in all API's

export const loginUser = async (userData) => {
  return await postRequest(ENDPOINTS.LOGIN, false, userData)
};

export const registerUser = async (userData) => {
  return await postRequest(ENDPOINTS.REGISTER, false, userData);
};

export const resetPassword = async (userData) => {
  return await postRequest(ENDPOINTS.RESET_PASSWORD, false, userData);
};

export const forgotPassword = async (userData) => {
  return await postRequest(ENDPOINTS.FORGOT_PASSWORD, false, userData);
};