import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/* Theme variables */
import './theme/variables.css';
import {ScooterProvider} from "./components/scooter/ScooterProvider";
import React from "react";
import EditScooter from "./components/scooter/EditScooter";
import {AuthProvider} from "./components/auth/AuthProvider";
import {Login} from "./components/auth/Login";
import {PrivateRoute} from "./components/auth/PrivateRoute";
import ScooterList from "./components/scooter/ScooterList";

const App: React.FC = () => (
  <IonApp>
    <IonReactRouter>
      <IonRouterOutlet>
        <AuthProvider>
          <Route path="/login" component={Login} exact={true}/>
          <ScooterProvider>
            <PrivateRoute path="/scooters" component={ScooterList} exact={true}/>
            <PrivateRoute path="/scooter" component={EditScooter} exact={true}/>
            <PrivateRoute path="/scooter/:id" component={EditScooter} exact={true}/>
          </ScooterProvider>
          <Route exact path="/" render={() => <Redirect to="/scooters"/>}/>
        </AuthProvider>
      </IonRouterOutlet>
    </IonReactRouter>
  </IonApp>
);

export default App;
