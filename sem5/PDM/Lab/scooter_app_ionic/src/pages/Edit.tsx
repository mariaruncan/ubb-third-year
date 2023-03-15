import React, {useCallback, useContext, useEffect, useState} from "react";
import {RouteComponentProps} from "react-router";
import {ScooterContext} from "../scooter/api/ScooterProvider";
import {getLogger} from "../core";
import {Scooter} from "../scooter/model/Scooter";
import {
    IonButton,
    IonButtons,
    IonContent,
    IonHeader,
    IonInput, IonItem, IonLabel, IonList,
    IonPage, IonProgressBar, IonSelect, IonSelectOption,
    IonTitle,
    IonToolbar
} from "@ionic/react";

const log = getLogger("Edit: ")

interface ScooterEditProps extends RouteComponentProps<{
    id?: string
}> {
}

const Edit: React.FC<ScooterEditProps> = ({history, match}) => {
    const {scooters, saving, savingError, saveScooter} = useContext(ScooterContext)
    const [batteryLevel, setBattery] = useState<number>(0)
    const [number, setNumber] = useState<number>(0)
    const [locked, setLocked] = useState<boolean>(false)
    const [scooter, setScooter] = useState<Scooter>()
    const scooterId = match.params.id

    useEffect(() => {
        log("useEffect");
        const scooter = scooters?.find(it => it._id === scooterId);
        setScooter(scooter);
        if (scooter) {
            setNumber(scooter.number)
            setLocked(scooter.locked)
            setBattery(scooter.batteryLevel);
        }
    }, [scooterId, scooters]);

    const handleSave = () => {
        const editedItem = scooter ? {...scooter, number, locked, batteryLevel} : {number, locked, batteryLevel};
        saveScooter && saveScooter(editedItem).then(() => history.goBack());
    };
    log('render');

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

export default Edit;