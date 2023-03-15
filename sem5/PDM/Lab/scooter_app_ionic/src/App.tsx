import {Redirect, Route} from 'react-router-dom';
import {IonApp, IonRouterOutlet, setupIonicReact} from '@ionic/react';
import {IonReactRouter} from '@ionic/react-router';
import {ScooterProvider} from "./scooter/api/ScooterProvider";
import Home from "./pages/Home";
import React, {ReactNode} from "react";
import Edit from "./pages/Edit";

import '@ionic/react/css/core.css';

import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

import './theme/variables.css';
import {AuthProvider} from "./auth/api/AuthProvider";
import {PrivateRoute} from "./auth/PrivateRoute";
import {Login} from "./pages/Login";

setupIonicReact();

const HomeFixed = Home as unknown as ReactNode
const EditFixed = Edit as unknown as ReactNode

const App: React.FC = () => (
    <IonApp>
        <IonReactRouter>
            <IonRouterOutlet>
                <AuthProvider>
                    <Route path="/login" component={Login} exact={true}/>
                    <ScooterProvider>
                        <PrivateRoute path="/scooters" component={HomeFixed} exact={true}/>
                        <PrivateRoute path="/scooter" component={EditFixed} exact={true}/>
                        <PrivateRoute path="/scooter/:id" component={EditFixed} exact={true}/>
                    </ScooterProvider>
                    <Route exact path="/" render={() => <Redirect to="/scooters"/>}/>
                </AuthProvider>
            </IonRouterOutlet>
        </IonReactRouter>
    </IonApp>
);

export default App;
