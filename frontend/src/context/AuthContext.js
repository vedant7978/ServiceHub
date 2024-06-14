import React, { createContext, useEffect, useState } from 'react';
import { AUTH_TOKEN_KEY } from '../utils/Constants';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authToken, setAuthToken] = useState(null);

    const storeAuthToken = (token) => {
        setAuthToken(token);
    };

    const deleteAuthToken = () => {
        setAuthToken(null);
    };

    useEffect(() => {
        let authToken = localStorage.getItem(AUTH_TOKEN_KEY);
        if (authToken) {
            setAuthToken(authToken);
        }
    }, [])

    return (
        <AuthContext.Provider value={{ authToken, storeAuthToken, deleteAuthToken }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;

