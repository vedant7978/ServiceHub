
export const AUTH_TOKEN_KEY = "AUTH_TOKEN";

// Define endpoints
export const ENDPOINTS = {
  LOGIN: "/api/auth/login",
  REGISTER: "/api/auth/register",
  RESET_PASSWORD:"/api/auth/reset-password",
  FORGOT_PASSWORD:"/api/auth/forgot-password",
  GET_USER_DATA: "/api/user-profile/get-user-details",
  UPDATE_INTO_PROFILE: "/api/user-profile/update-user-details",
  GET_FEEDBACK: "/api/user/get-feedbacks",
  GET_CONTRACTS: "/api/contract/get-contracts"
  // Add other endpoints here
};

// const BASE_URL = "http://csci5308-vm8.research.cs.dal.ca:8080";
// Comment above URL and uncomment this when running backend locally
export const BASE_URL = "http://127.0.0.1:8080";
