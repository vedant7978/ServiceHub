import React, { createContext, useContext, useEffect } from 'react';
import axios from 'axios';
import AuthContext from './AuthContext';
import { BASE_URL } from '../utils/Constants';

const AxiosContext = createContext();

export const AxiosProvider = ({ children }) => {
  const { authToken } = useContext(AuthContext);
  let axiosConfig = {
    baseURL: BASE_URL,
    headers: {
      "Content-Type": "application/json",
      "Accept": "application/json"
    },
  }
  const axiosInstance = axios.create(axiosConfig);
  const axiosInstanceWithAuth = axios.create(axiosConfig);
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
  }
  authInterceptor();

  useEffect(() => {
    authInterceptor();
  }, [authToken])

  const getRequest = async (endpoint, useAuthToken = false, params = {}) => {
    try {
      const instance = useAuthToken ? axiosInstanceWithAuth : axiosInstance;
      return await instance.get(endpoint, { params });
    } catch (error) {
      console.error(`Error in GET request to ${endpoint}:`, error);
      throw error;
    }
  };

  const postRequest = async (endpoint, useAuthToken = false, data) => {
    try {
      const instance = useAuthToken ? axiosInstanceWithAuth : axiosInstance;
      return await instance.post(endpoint, data);
    } catch (error) {
      console.error(`Error in POST request to ${endpoint}:`, error);
      throw error;
    }
  };

  // Generic function to handle POST requests
  const putRequest = async (endpoint, useAuthToken = false, data) => {
    try {
      const instance = useAuthToken ? axiosInstanceWithAuth : axiosInstance;
      return await instance.put(endpoint, data);
    } catch (error) {
      console.error(`Error in PUT request to ${endpoint}:`, error);
      throw error;
    }
  };

  return (
    <AxiosContext.Provider value={{ getRequest, postRequest, putRequest }}>
      {children}
    </AxiosContext.Provider>
  );
};

export const useAxios = () => useContext(AxiosContext);

export default AxiosContext;
