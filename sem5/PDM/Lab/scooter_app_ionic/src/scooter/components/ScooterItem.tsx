import React, {memo, useCallback} from 'react';
import {IonCard, IonCardContent, IonCardHeader, IonCardSubtitle, IonCardTitle, IonItem, IonLabel} from '@ionic/react';
import {Scooter} from "../model/Scooter";

interface ScooterPropsExt extends Scooter {
    onEdit: (_id?: string) => void;
}

const ScooterItem: React.FC<ScooterPropsExt> = ({_id, number, locked, batteryLevel, onEdit}) => {
    const handleEdit = useCallback(() => onEdit(_id), [_id, onEdit])

    return (
        <IonItem onClick={handleEdit}>
            <IonCard>
                <IonCardHeader>
                    <IonCardTitle>Scooter: {number}</IonCardTitle>
                </IonCardHeader>
                <IonCardContent>id: {String(_id)}</IonCardContent>
                <IonCardContent>locked: {String(locked)}</IonCardContent>
                <IonCardContent>batteryLevel: {batteryLevel}</IonCardContent>
            </IonCard>
        </IonItem>
    );
};

export default memo(ScooterItem);
