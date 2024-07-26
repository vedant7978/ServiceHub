import React, { createContext, useContext, useEffect, useState } from 'react';
import { AUTH_TOKEN_KEY } from '../utils/Constants';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authToken, setAuthToken] = useState(null);
    const [isUserLoggedIn, setIsUserLoggedIn] = useState(!!localStorage.getItem(AUTH_TOKEN_KEY));

    const setUserLoggedIn = (token) => {
        setAuthToken(token);
        localStorage.setItem(AUTH_TOKEN_KEY, token);
        setIsUserLoggedIn(true);
    };

    const setUserLoggedOut = () => {
        setAuthToken(null);
        setIsUserLoggedIn(false);
        localStorage.removeItem(AUTH_TOKEN_KEY);
    };

    useEffect(() => {
        let authToken = localStorage.getItem(AUTH_TOKEN_KEY);
        if (authToken) {
            setAuthToken(authToken);
            setIsUserLoggedIn(true);
        }
    }, [])

    return (
      <AuthContext.Provider
        value={{
            authToken,
            setUserLoggedIn,
            setUserLoggedOut,
            isUserLoggedIn,
        }}>
          {children}
      </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
