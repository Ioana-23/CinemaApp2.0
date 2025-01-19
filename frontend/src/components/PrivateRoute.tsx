import React, {useState} from 'react';
import {Route} from 'react-router-dom';
import PropTypes from "prop-types";
import {ReadMore} from "./ReadMore.tsx";
import { Navigate } from 'react-router-dom';
export interface PrivateRouteProps {
    children: PropTypes.ReactNodeLike,
    component: any;
    path: string;
    exact?: boolean;
}

export const PrivateRoute: React.FC<PrivateRouteProps> = ({ component: Component, children, ...rest }) => {
    const [ isAuthenticated ] = useState<boolean>(true);
    return (
        <Route {...rest} render={props => {
            if (isAuthenticated) {
                return <Component {...props} />;
            }
            return <Navigate to="/"/>
        }}/>
    );
}
