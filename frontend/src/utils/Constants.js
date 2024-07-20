export const AUTH_TOKEN_KEY = "AUTH_TOKEN";

// Define endpoints
export const ENDPOINTS = {
  LOGIN: "/api/auth/login",
  REGISTER: "/api/auth/register",
  RESET_PASSWORD: "/api/auth/reset-password",
  FORGOT_PASSWORD: "/api/auth/forgot-password",
  GET_USER_DATA: "/api/user-profile/get-user-details",
  UPDATE_INTO_PROFILE: "/api/user-profile/update-user-details",
  GET_FEEDBACK: "/api/feedback/get-feedbacks",
  GET_PENDING_CONTRACTS: "/api/contract/get-pending-contracts",
  GET_HISTORY_CONTRACTS: "/api/contract/get-history-contracts",
  ADD_WISHLIST: "/api/wishlist/add-wishlist",
  GET_WISHLISTED_SERVICES:"/api/wishlist/get-wishlist",
  PROVIDER_DETAILS: "/api/service/provider-details",
  GET_SEARCH_SERVICES:"api/service/search-services",
  ACCEPT_CONTRACT: "/api/contract/accept-contract",
  REJECT_CONTRACT: "/api/contract/reject-contract",
  SIGN_OUT: "/api/auth/sign-out",
  GET_CONTRACT_FEEDBACK: "/api/contract-feedback/get-contract-feedback",
  ADD_CONTRACT_FEEDBACK: "/api/contract-feedback/add-contract-feedback",
  GET_SERVICES: "/api/service/get-user-services",
  ADD_SERVICE: "/api/service/add-service",
  UPDATE_SERVICE: "/api/service/update-service",
  DELETE_SERVICE: "/api/service/delete-service",
  NEW_PASSWORD: "/api/user-profile/new-password",
  UPLOAD_FILE: "/api/upload"
  // Add other endpoints here
};

export const ServiceType = {
  "1": "HomeServices",
  "2": "PersonalServices",
  "3": "ProfessionalServices",
  "4": "EducationalServices",
  "5": "TechnicalServices",
  "6": "EventServices",
  "7": "TransportationServices",
  "8": "HealthAndWellness",
  "9": "CreativeServices",
  "10": "LegalAndFinancialServices",
  "11": "Plumbing",
  "12": "Electrician",
  "13": "Other"
}

// const BASE_URL = "http://csci5308-vm8.research.cs.dal.ca:8080";
// Comment above URL and uncomment this when running backend locally
export const BASE_URL = "http://localhost:8080"; 
