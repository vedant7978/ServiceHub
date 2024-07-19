import axios from 'axios';
import React, { createContext, useContext, useEffect } from 'react';
import { BASE_URL, ENDPOINTS } from '../utils/Constants';
import { useAuth } from './AuthContext';

const AxiosContext = createContext();

export const AxiosProvider = ({ children }) => {
  const { authToken } = useAuth();
  let axiosConfig = {
    baseURL: BASE_URL,
    headers: {
      "Content-Type": "application/json",
      "Accept": "application/json"
    },
  }
  let multipartAxiosConfig = {
    baseURL: BASE_URL,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }

  const axiosInstance = axios.create(axiosConfig);
  const axiosInstanceWithAuth = axios.create(axiosConfig);
  const multipartAxiosInstanceWithAuth = axios.create(multipartAxiosConfig);

  const authInterceptor = () => {
    axiosInstanceWithAuth.interceptors.request.use(
      (config) => {
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
    multipartAxiosInstanceWithAuth.interceptors.request.use(
      (config) => {
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
  }

  useEffect(() => {
    authInterceptor();
  }, [authToken])

  // Generic function to handle GET requests
  const getRequest = async (endpoint, useAuthToken = false, params = {}) => {
    try {
      const instance = useAuthToken ? axiosInstanceWithAuth : axiosInstance;
      return await instance.get(endpoint, { params });
    } catch (error) {
      console.error(`Error in GET request to ${endpoint}:`, error);
      throw error;
    }
  };

  // Generic function to handle POST requests
  const postRequest = async (endpoint, useAuthToken = false, data) => {
    try {
      const instance = useAuthToken ? axiosInstanceWithAuth : axiosInstance;
      return await instance.post(endpoint, data);
    } catch (error) {
      console.error(`Error in POST request to ${endpoint}:`, error);
      throw error;
    }
  };

  // Generic function to handle PUT requests
  const putRequest = async (endpoint, useAuthToken = false, data) => {
    try {
      const instance = useAuthToken ? axiosInstanceWithAuth : axiosInstance;
      return await instance.put(endpoint, data);
    } catch (error) {
      console.error(`Error in PUT request to ${endpoint}:`, error);
      throw error;
    }
  };

  // Generic function to handle DELETE requests
  const deleteRequest = async (endpoint, useAuthToken = false, params = {}) => {
    try {
      const instance = useAuthToken ? axiosInstanceWithAuth : axiosInstance;
      return await instance.delete(endpoint, { params });
    } catch (error) {
      console.error(`Error in DELETE request to ${endpoint}:`, error);
      throw error;
    }
  };

  const uploadFile = async (data) => {
    try {
      return await multipartAxiosInstanceWithAuth.post(ENDPOINTS.UPLOAD_FILE, data);
    } catch (error) {
      console.error(`Error in uploading file:`, error);
      throw error;
    }
  }

  return (
    <AxiosContext.Provider value={{ getRequest, postRequest, putRequest, deleteRequest, uploadFile }}>
      {children}
    </AxiosContext.Provider>
  );
};

export const useAxios = () => useContext(AxiosContext);
