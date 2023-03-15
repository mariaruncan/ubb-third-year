import React from 'react';
import {
    IonCard, IonCardContent,
    IonCardHeader,
    IonCardSubtitle, IonCardTitle, IonImg,
    IonItem, IonLabel
} from '@ionic/react';
import {ScooterProps} from './ScooterProps';

interface ScooterPropsExt extends ScooterProps {
    onEdit: (_id?: string) => void;
}

const Scooter: React.FC<ScooterPropsExt> = ({
                                                _id, number,
                                                locked, batteryLevel,
                                                latitude, longitude,
                                                photoPath, onEdit
                                            }) => {
    return (
        <IonItem onClick={() => onEdit(_id)}>
            <IonCard>
                <IonImg alt="NO PHOTO" style={{width: "100px", height: "100px"}} src={photoPath}/>
                <IonCardHeader>
                    <IonCardTitle>{number}</IonCardTitle>
                    <IonCardSubtitle>{locked.toString()}</IonCardSubtitle>
                </IonCardHeader>
                <IonLabel>id {_id}</IonLabel>
                <IonLabel>number {number}</IonLabel>
                <IonLabel>batteryLevel {batteryLevel}</IonLabel>
                <IonLabel>lat: {latitude}</IonLabel>
                <IonLabel>lng: {longitude}</IonLabel>
            </IonCard>
        </IonItem>
    );
};

export default Scooter;
