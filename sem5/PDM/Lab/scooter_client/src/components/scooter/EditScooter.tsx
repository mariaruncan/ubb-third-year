import React, {useContext, useEffect, useState} from 'react';

import {
    IonButton,
    IonButtons,
    IonContent,
    IonHeader,
    IonInput,
    IonItem,
    IonLabel, IonList,
    IonPage, IonProgressBar, IonSelect, IonSelectOption,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {RouteComponentProps} from 'react-router';

import {ScooterContext} from "./ScooterProvider";
import {ScooterProps} from "./Scooter";
import {AuthContext} from "../auth/AuthProvider";


interface EditScooterProps extends RouteComponentProps<{
    id?: string;
}> {
}

const EditScooter: React.FC<EditScooterProps> = ({history, match}) => {

    const {scooters, saving, savingError, saveScooter} = useContext(ScooterContext);
    const {token} = useContext(AuthContext);
    const [batteryLevel, setBattery] = useState<number>(0)
    const [number, setNumber] = useState<number>(0)
    const [locked, setLocked] = useState<boolean>(false)
    const [scooter, setScooter] = useState<ScooterProps>()
    const [scooterState, setScooterState] = useState<boolean>(false);
    const scooterId = match.params.id

    useEffect(() => {
        const routeId = match.params.id || '';
        const scooter = scooters?.find(it => it._id === routeId);
        setScooter(scooter);
        if (scooter && !scooterState) {
            setNumber(scooter.number)
            setLocked(scooter.locked)
            setBattery(scooter.batteryLevel);

            setScooterState(true)
        }
    }, [match.params.id, scooters]);
    const handleSave = () => {
        const editedScooter = scooter ? {...scooter, number, locked, batteryLevel} : {number, locked, batteryLevel};
        saveScooter && saveScooter(editedScooter).then(() => history.goBack());
    };
    const handleBack = () => {
        history.goBack()
    }

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    {
                        (scooterId && <IonTitle>Edit scooter with id {scooterId}</IonTitle>) ||
                        <IonTitle>Add scooter</IonTitle>
                    }
                    <IonButtons slot="end">
                        <IonButton onClick={handleSave} disabled={saving}>
                            Save
                        </IonButton>
                    </IonButtons>
                    {saving && <IonProgressBar type={"indeterminate"}/>}
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonList>
                    <IonItem>
                        <IonLabel>Enter battery level: </IonLabel>
                        <IonInput value={batteryLevel} type={"number"}
                                  onIonChange={e => setBattery(Number(e.detail.value) || 0)}/>
                    </IonItem>
                    <IonItem>
                        <IonLabel>Select locked status:</IonLabel>
                        <IonSelect interface="popover" value={locked}
                                   onIonChange={e => setLocked(Boolean(e.detail.value))}>
                            <IonSelectOption value={true}>true</IonSelectOption>
                            <IonSelectOption value={false}>false</IonSelectOption>
                        </IonSelect>
                    </IonItem>
                </IonList>
                {savingError && (
                    <div>{savingError.message || 'Failed to save item'}</div>
                )}
            </IonContent>
        </IonPage>
    );
};

export default EditScooter;
