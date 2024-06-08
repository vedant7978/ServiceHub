import React from 'react';
import NavBar from "../components/navbar/NavBar";

export const WithNavBar = ({children}) => {
  return (
    <>
      <NavBar/>
      <div>{children}</div>
    </>
  );
};
