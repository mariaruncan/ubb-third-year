import React, {useCallback} from "react";
import {IonCard, IonCardContent, IonCardHeader, IonCardTitle, IonItem} from "@ionic/react";

interface ScooterPropsExt extends ScooterProps {
    onEdit: (_id?: string) => void;
}

export interface ScooterProps {
    _id?: string;
    number: number;
    locked: boolean;
    batteryLevel: number;
    updatedAt?: Date;
}

const Scooter: React.FC<ScooterPropsExt> = ({_id, number, locked, batteryLevel, updatedAt,onEdit}) => {
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

export default Scooter;
