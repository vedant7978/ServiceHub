import {ENDPOINTS, getRequest} from "./ApiModule";

// This file includes all API's of the application regarding the FEEDBACK module
// Feedback API include /api/user as prefix in all API's

export const getFeedbacks = async (userData) => {
  return await getRequest(ENDPOINTS.GET_FEEDBACK, true, userData);
};