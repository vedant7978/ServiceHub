import {ENDPOINTS, getRequest, putRequest} from "./ApiModule";

// This file includes all API's of the application regarding the Profile module
// Profile API include /api/profile as prefix in all API's

export const getUser = async (userData) => {
  return await getRequest(ENDPOINTS.GET_USER_DATA, true, userData);
};

export const updateUser = async (userData) => {
  return await putRequest(ENDPOINTS.UPDATE_INTO_PROFILE, true, userData);
};

