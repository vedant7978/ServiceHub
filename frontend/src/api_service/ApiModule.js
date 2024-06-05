import axios from "axios";
import Constants from "../utils/Constants";

// [TODO]: uncomment csci vm8 base url and remove localhost url once CORS policy issue fix is deployed
// const BASE_URL = "http://csci5308-vm8.research.cs.dal.ca:8080";
const BASE_URL = "http://127.0.0.1:8080"; // Currently running locally as CORS policy fix is not yet deployed

// Define endpoints
export const ENDPOINTS = {
  LOGIN: "/api/auth/login",
  REGISTER: "/api/auth/register",
  // Add other endpoints here
};

const apiInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
});

const apiInstanceWithAuthToken = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
});

// Add interceptor to include authorization token
apiInstanceWithAuthToken.interceptors.request.use(
  (config) => {
    const authToken = localStorage.getItem(Constants.AUTH_TOKEN_KEY);
    if (authToken) {
      config.headers.Authorization = `Bearer ${authToken}`;
    }
    return config;
  },
  (error) => {
    console.error("Request interceptor error:", error);
    return Promise.reject(error);
  }
);

// Generic function to handle GET requests
export const getRequest = async (
  endpoint,
  useAuthToken = false,
  params = {}
) => {
  try {
    const instance = useAuthToken ? apiInstanceWithAuthToken : apiInstance;
    return await instance.get(endpoint, { params });
  } catch (error) {
    console.error(`Error in GET request to ${endpoint}:`, error);
    throw error;
  }
};

// Generic function to handle POST requests
export const postRequest = async (endpoint, useAuthToken = false, data) => {
  try {
    const instance = useAuthToken ? apiInstanceWithAuthToken : apiInstance;
    return await apiInstance.post(endpoint, data);
  } catch (error) {
    console.error(`Error in POST request to ${endpoint}:`, error);
    throw error;
  }
};
