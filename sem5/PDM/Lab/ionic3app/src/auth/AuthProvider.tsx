import React, {useCallback, useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import {getLogger} from '../core';
import {login as loginApi} from './authApi';
import { Preferences } from '@capacitor/preferences';

const log = getLogger('AuthProvider');

type LoginFn = (username?: string, password?: string) => void;
type TokenFn = (token?: string) => void;
type LogoutFn = () => void;

export interface AuthState {
    authenticationError: Error | null;
    isAuthenticated: boolean;
    isAuthenticating: boolean;
    login?: LoginFn;
    setToken?: TokenFn;
    logout?: LogoutFn;
    pendingAuthentication?: boolean;
    username?: string;
    password?: string;
    token: string;
}

const initialState: AuthState = {
    isAuthenticated: false,
    isAuthenticating: false,
    authenticationError: null,
    pendingAuthentication: false,
    token: '',
};

export const AuthContext = React.createContext<AuthState>(initialState);

interface AuthProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const [state, setState] = useState<AuthState>(initialState);
    const {isAuthenticated, isAuthenticating, authenticationError, pendingAuthentication, token} = state;
    const login = useCallback<LoginFn>(loginCallback, []);
    const logout = useCallback<LogoutFn>(logoutCallback, []);
    useEffect(authenticationEffect, [pendingAuthentication]);
    const value = {isAuthenticated, login, isAuthenticating, authenticationError, token, logout, setToken};
    log('render');

    useEffect(() => {
        const onUseEffect = async () => {
            const res = await Preferences.get({key: 'token'});
            if (res.value) {
                setState({
                    ...state,
                    isAuthenticated: true,
                });
            }
        }
        onUseEffect().then();
    }, [])

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );

    function logoutCallback(): void {
        log('logout');
        removeFromStorage().then();
        setState({
            ...state,
            isAuthenticated: false,
            isAuthenticating: false,
            token: ''
        });

        async function removeFromStorage() {
           await Preferences.remove({'key': 'movies'});
        }
    }

    function setToken(token?: string): void {
        if (token)
            setState({
                ...state,
                token
            });
    }

    function loginCallback(username?: string, password?: string): void {
        log('login');
        setState({
            ...state,
            pendingAuthentication: true,
            username,
            password
        });
    }

    function authenticationEffect() {
        let canceled = false;
        authenticate();
        return () => {
            canceled = true;
        }

        async function authenticate() {
            if (!pendingAuthentication) {
                log('authenticate, !pendingAuthentication, return');
                return;
            }
            try {
                log('authenticate...');
                setState({
                    ...state,
                    isAuthenticating: true,
                });
                const {username, password} = state;
                const {token} = await loginApi(username, password);
                await Preferences.set({key: 'token', value: token});
                if (canceled) {
                    return;
                }
                log('authenticate succeeded');
                setState({
                    ...state,
                    token,
                    pendingAuthentication: false,
                    isAuthenticated: true,
                    isAuthenticating: false,
                });
            } catch (error) {
                if (canceled) {
                    return;
                }
                log('authenticate failed');
                setState({
                    ...state,
                    authenticationError: error,
                    pendingAuthentication: false,
                    isAuthenticating: false,
                });
            }
        }
    }
};
