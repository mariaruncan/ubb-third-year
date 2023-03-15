import {getLogger} from "../core";
import {RouteComponentProps} from "react-router";
import React, {useContext, useState} from "react";
import {ScooterContext} from "../scooter/api/ScooterProvider";
import {
    IonButton,
    IonButtons,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader, IonIcon, IonInput, IonLabel,
    IonList,
    IonLoading,
    IonPage, IonSelect, IonSelectOption,
    IonTitle,
    IonToolbar
} from "@ionic/react";
import Scooter from "../scooter/components/ScooterItem";
import {add} from "ionicons/icons";
import {useNetwork} from "../network/useNetwork";
import {useBackgroundTask} from "../network/useBackgroundTask";

const log = getLogger('ScooterList');

const Home: React.FC<RouteComponentProps> = ({history}) => {
    const {networkStatus} = useNetwork();
    useBackgroundTask(() => new Promise(resolve => {
        console.log('My Background Task');
        resolve();
    }));
    const {scooters, fetching, fetchingError} = useContext(ScooterContext);

    const [scootNumber, setScootNumber] = useState<string>("")
    const [scootLocked, setLocked] = useState<boolean>(false)

    log('render');
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Scooter App</IonTitle>
                    {
                        (networkStatus.connected && <IonTitle>Online</IonTitle>) || <IonTitle>Offline</IonTitle>
                    }
                </IonToolbar>
                <IonInput value={scootNumber} onIonChange={e => setScootNumber(e.detail.value || "")}/>
                <IonSelect interface="popover" value={scootLocked}
                           onIonChange={e => setLocked(Boolean(e.detail.value))}>
                    <IonSelectOption value={true}>true</IonSelectOption>
                    <IonSelectOption value={false}>false</IonSelectOption>
                </IonSelect>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching items"/>
                {scooters && (
                    <IonList>
                        {scooters.map(({_id, number, locked, batteryLevel, updatedAt}) =>
                            number.toString().startsWith(scootNumber.toString()) && locked === scootLocked &&
                            <Scooter key={_id}
                                     _id={_id}
                                     number={number}
                                     locked={locked}
                                     batteryLevel={batteryLevel}
                                     updatedAt={updatedAt}
                                     onEdit={id => history.push(`/scooter/${id}`)}
                            />)}
                    </IonList>
                )}
                {fetchingError && (
                    <div>{fetchingError.message || 'Failed to fetch items'}</div>
                )}
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push("/scooter")}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default Home;