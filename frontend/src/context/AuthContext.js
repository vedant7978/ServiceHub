import { jwtDecode } from "jwt-decode";
import React, { createContext, useContext, useEffect, useState } from 'react';
import { AUTH_TOKEN_KEY } from '../utils/Constants';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authToken, setAuthToken] = useState(null);
    const [loggedInUserEmail, setLoggedInUserEmail] = useState(null);

    const storeAuthToken = (token) => {
        setAuthToken(token);
        localStorage.setItem(AUTH_TOKEN_KEY, token);
        const decodedToken = jwtDecode(token);
        setLoggedInUserEmail(decodedToken.sub);
    };

    const deleteAuthToken = () => {
        setAuthToken(null);
        setLoggedInUserEmail(null);
        localStorage.removeItem(AUTH_TOKEN_KEY);
    };

    useEffect(() => {
        let authToken = localStorage.getItem(AUTH_TOKEN_KEY);
        if (authToken) {
            setAuthToken(authToken);
            const decodedToken = jwtDecode(authToken);
            setLoggedInUserEmail(decodedToken.sub);
        }
    }, [])

    return (
      <AuthContext.Provider
        value={{
            authToken,
            storeAuthToken,
            deleteAuthToken,
            loggedInUserEmail,
        }}>
          {children}
      </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
