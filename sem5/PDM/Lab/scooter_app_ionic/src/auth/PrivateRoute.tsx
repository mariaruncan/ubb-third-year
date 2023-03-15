import {getLogger} from "../core";
import React, {useContext} from "react";
import {AuthContext, AuthState} from "./api/AuthProvider";
import {Redirect, Route} from "react-router-dom";
import PropTypes from "prop-types";

const log = getLogger("LOGIN: ");

export interface PrivateRouteProps {
    component: PropTypes.ReactNodeLike;
    path: string;
    exact?: boolean;
}

export const PrivateRoute: React.FC<PrivateRouteProps> = ({component: Component, ...rest}) => {
    const {isAuthenticated} = useContext<AuthState>(AuthContext);
    log('render, isAuthenticated', isAuthenticated);
    return (
        <Route {...rest} render={props => {
            if (isAuthenticated) {
                // @ts-ignore
                return <Component {...props} />;
            }
            return <Redirect to={{pathname: '/login'}}/>
        }}/>
    );
}
