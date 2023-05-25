import React from 'react'
import { useContext } from "react";
import AuthContext from "./auth-context";
import { Navigate, Outlet } from "react-router-dom";

const ProtectedRoute = () => {
    const { isLoggedIn } = useContext(AuthContext)
    return isLoggedIn ? <Outlet /> : <Navigate to="/" />
};

export default ProtectedRoute;