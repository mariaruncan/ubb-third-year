import React from 'react';
import { Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';
import './css/Scooter.css'
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
import { AuthProvider, Login, PrivateRoute } from './auth';
import {ScooterProvider} from "./scooters/ScooterProvider";
import ScooterList from "./scooters/ScooterList";
import ScooterEdit from "./scooters/ScooterEdit";

// @ts-ignore
const App: React.FC = () => (
  <IonApp>
    <IonReactRouter>
      <IonRouterOutlet>
        <AuthProvider>
          <Route path="/" component={Login} exact={true}/>
          <ScooterProvider>
            <PrivateRoute path="/items" component={ScooterList} exact={true}/>
            <PrivateRoute path='/items/:id' component={ScooterEdit} />
            <PrivateRoute path='/items/add' component={ScooterEdit} />
          </ScooterProvider>
        </AuthProvider>
      </IonRouterOutlet>
    </IonReactRouter>
  </IonApp>
);

export default App;
