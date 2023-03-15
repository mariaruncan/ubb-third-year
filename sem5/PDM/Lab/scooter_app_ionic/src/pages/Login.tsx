import {getLogger} from "../core";
import React, {useContext, useState} from "react";
import {RouteComponentProps} from "react-router";
import {AuthContext} from "../auth/api/AuthProvider";
import {LoginState} from "../auth/model/LoginState";
import {Redirect} from "react-router-dom";
import {IonButton, IonContent, IonHeader, IonLoading, IonPage, IonTitle, IonInput, IonToolbar} from "@ionic/react";

const log = getLogger("LOGIN: ")

export const Login: React.FC<RouteComponentProps> = () => {
    const {isAuthenticated, isAuthenticating, login, authenticationError} = useContext(AuthContext);
    const [state, setState] = useState<LoginState>({});
    const {username, password} = state;
    const handleLogin = () => {
        log('handleLogin...');
        login?.(username, password);
    };
    log('render');

    if (isAuthenticated) {
        return <Redirect to={{pathname: '/'}}/>
    }

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Login</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonInput
                    placeholder="Username"
                    value={username}
                    onIonChange={e => setState({
                        ...state,
                        username: e.detail.value || ''
                    })}/>
                <IonInput
                    placeholder="Password"
                    value={password}
                    onIonChange={e => setState({
                        ...state,
                        password: e.detail.value || ''
                    })}/>
                <IonLoading isOpen={isAuthenticating}/>
                {authenticationError && (
                    <div>{authenticationError.message || 'Failed to authenticate'}</div>
                )}
                <IonButton onClick={handleLogin}>Login</IonButton>
            </IonContent>
        </IonPage>
    );
}