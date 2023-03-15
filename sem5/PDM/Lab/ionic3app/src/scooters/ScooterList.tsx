import {
    createAnimation,
    IonButton,
    IonButtons,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader, IonIcon, IonInfiniteScroll, IonInfiniteScrollContent,
    IonList,
    IonLoading,
    IonPage, IonToolbar
} from '@ionic/react';
import React, {useContext, useEffect, useState} from "react";
import {RouteComponentProps} from "react-router";
import {add} from "ionicons/icons";
import {getLogger} from "../core";
import {ScooterContext} from "./ScooterProvider";
import Scooter from "../scooters/Scooter";
import {AuthContext} from "../auth";
import {usePhotos} from "../photo/usePhotos";

const log = getLogger('ScooterList');
const ScooterList: React.FC<RouteComponentProps> = ({history}) => {
    const {scooters, fetching, fetchingError, fetchScooters, resetLogout} = useContext(ScooterContext);
    const {photos, takePhoto, deletePhoto} = usePhotos();
    const [searchScooters, setSearchScooters] = useState<boolean>(false);
    const {isAuthenticated, logout} = useContext(AuthContext);
    const [lockeds, setLockeds] = useState<boolean[]>([true, false]);
    const [locked, setLocked] = useState<boolean>(false);
    const [disableInfiniteScroll, setDisableInfiniteScroll] = useState<boolean>(false);

    const handleLogout = () => {
        if (isAuthenticated) {
            resetLogout?.();
            logout?.();
        }
    }

    function searchNext($event: CustomEvent<void>) {
        fetchScooters?.(locked ? locked : false);
        ($event.target as HTMLIonInfiniteScrollElement).complete();
    }

    function onSearchChanged(locked: boolean) {
        log('search ')
        setSearchScooters(locked);
        fetchScooters?.(locked);
    }

    useEffect(simpleAnimationJS, []);

    function simpleAnimationJS() {
        const el = document.querySelector('.square-a');
        if (el) {
            const animation = createAnimation()
                .addElement(el)
                .duration(1000)
                .iterations(Infinity)
                .keyframes([
                    { offset: 0, color: 'red' },
                    { offset: 0.72, color: 'var(--background)' },
                    { offset: 1, color: 'green' }
                ]);
            animation.play();
        }
    }

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <div className="square-a">
                        <p>Scooters</p>
                    </div>
                    <IonButtons slot="end">
                        <IonButton routerLink='/' routerDirection='root' onClick={handleLogout}>
                            Logout
                        </IonButton>
                        <IonButton onClick={() => history.push('/items/add')}>
                            Add
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonLoading isOpen={fetching} message="Fetching items"/>
                {scooters && (
                    <IonList>
                        {scooters.map(({
                                           _id, number, locked,
                                           batteryLevel, latitude, longitude, photoPath,
                                       }) => {
                                let photoView = "";
                                for (let photo of photos) {
                                    if (photo.filepath === photoPath) {
                                        photoView = photo.webviewPath ? photo.webviewPath : "";
                                    }
                                }

                                return <Scooter key={_id}
                                                _id={_id}
                                                number={number}
                                                locked={locked}
                                                batteryLevel={batteryLevel}
                                                latitude={latitude}
                                                longitude={longitude}
                                                onEdit={id => history.push(`/items/${id}`)}
                                                photoPath={photoView}/>
                            }
                        )}
                    </IonList>
                )}
                {fetchingError && (
                    <div>{fetchingError.message || 'Failed to fetch items'}</div>
                )}
                <IonInfiniteScroll threshold="100px" disabled={disableInfiniteScroll}
                                   onIonInfinite={(e: CustomEvent<void>) => {
                                       searchNext(e)
                                   }}>
                    <IonInfiniteScrollContent
                        loadingText="Loading more good books...">
                    </IonInfiniteScrollContent>
                </IonInfiniteScroll>
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/items/add')}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default ScooterList;
