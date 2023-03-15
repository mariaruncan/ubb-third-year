import React, {useContext, useEffect, useRef, useState} from 'react';
import {
    IonButton,
    IonButtons, IonCardContent,
    IonContent, IonFab, IonFabButton,
    IonHeader, IonIcon, IonImg,
    IonInput, IonItem, IonLabel,
    IonLoading, IonModal,
    IonPage, IonSelect, IonSelectOption, IonText,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {createAnimation, Animation} from "@ionic/core";
import {getLogger} from '../core';
import {ScooterContext} from './ScooterProvider';
import {RouteComponentProps} from 'react-router';
import {ScooterProps} from './ScooterProps';
// import {useNetwork} from "../network/useNetwork";
import {Preferences} from '@capacitor/preferences';
import {camera} from "ionicons/icons";
import {MyPhoto, usePhotos} from "../photo/usePhotos";
import MyMap from "../map/MyMap";

const log = getLogger('ScooterEdit');

interface ScooterEditProps extends RouteComponentProps<{
    id?: string;
}> {
}

const ScooterEdit: React.FC<ScooterEditProps> = ({history, match}) => {
    // const { networkStatus } = useNetwork();
    const {scooters, saving, savingError, saveScooter} = useContext(ScooterContext);
    const [number, setNumber] = useState(99999);
    const [locked, setLocked] = useState(false);
    const [batteryLevel, setBatteryLevel] = useState(0);
    const [scooter, setScooter] = useState<ScooterProps>();
    const [photoToDelete, setPhotoToDelete] = useState<MyPhoto>();
    const [photoPath, setPhotoPath] = useState('');
    const [photoFile, setPhotoFile] = useState('');
    const [offlineText, setOfflineText] = useState("");
    const {photos, takePhoto, deletePhoto} = usePhotos();
    const [latitude, setLatitude] = useState(47.640450);
    const [longitude, setLongitude] = useState(26.246936);

    useEffect(() => {
        const routeId = match.params.id || '';
        const m = scooters?.find(mv => mv._id == routeId);
        setScooter(m);

        if (m) {
            let photoView = "";
            getPhotoView(m.photoPath).then((data) => {
                photoView = data
                console.log('ss ', data)
            })
            setNumber(m.number);
            setLocked(m.locked);
            setBatteryLevel(m.batteryLevel);
            setPhotoPath(photoView);
            setPhotoFile(m.photoPath);
            setLatitude(m.latitude);
            setLongitude(m.longitude);
        }
    }, [match.params.id]);

    const getPhotoView = async (photoPath: string) => {
        const res = await Preferences.get({key: 'photos'});
        let photoView = "";

        if (res.value) {
            let photosFromStorage: MyPhoto[] = JSON.parse(res.value);
            log(photosFromStorage)

            for (let photo of photosFromStorage) {
                if (photo.filepath === photoPath) {
                    photoView = photo.webviewPath ? photo.webviewPath : "";
                    setPhotoPath(photoView)
                }
            }
        }
        return photoView;
    }

    const handleSave = () => {
        const scoot: ScooterProps = {
            'photoPath': photoFile,
            '_id': scooter?._id,
            'number': number,
            'batteryLevel': batteryLevel,
            'locked': locked,
            'userId': '',
            'latitude': latitude,
            'longitude': longitude
        };
        const editedScooter = scooter ? scoot : scoot;
        setScooter(editedScooter);
        saveScooter?.(editedScooter);
        // if (!networkStatus.connected) {
        //     log('save scooter in storage')
        //     setOfflineText("Your items are saved only locally");
        // }
    };

    const setMapPosition = (e: any) => {
        log('map position')
        console.log(e.latitude)
        console.log(e.longitude)
        setLatitude(e.latitude);
        setLongitude(e.longitude);
    }

    function mapLog(source: string) {
        return (e: any) => console.log(source, e.latLng.lat(), e.latLng.lng());
    }

    const [showModal, setShowModal] = useState(false);

    const enterAnimation = (baseEl: any) => {
        const backdropAnimation = createAnimation()
            .addElement(baseEl.querySelector("ion-backdrop")!)
            .fromTo("opacity", "0.01", "var(--backdrop-opacity)");

        const wrapperAnimation = createAnimation()
            .addElement(baseEl.querySelector(".modal-wrapper")!)
            .keyframes([
                {offset: 0, opacity: "0", transform: "scale(0)"},
                {offset: 1, opacity: "0.99", transform: "scale(1)"},
            ]);

        return createAnimation()
            .addElement(baseEl)
            .easing("ease-out")
            .duration(500)
            .addAnimation([backdropAnimation, wrapperAnimation]);
    };

    const leaveAnimation = (baseEl: any) => {
        return enterAnimation(baseEl).direction("reverse");
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Edit</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={handleSave}>
                            Save
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonItem>
                    <IonLabel>Number:</IonLabel>
                    <IonInput value={number} onIonChange={e => setNumber(Number(e.detail.value) || 99999)}/>
                </IonItem>
                <IonItem>
                    <IonLabel>Locked:</IonLabel>
                    <IonSelect value={locked} placeholder="Select locked" onIonChange={e => setLocked(e.detail.value)}>
                        <IonSelectOption key={"true"} value={true}>{"true"}</IonSelectOption>
                        <IonSelectOption key={"false"} value={false}>{"false"}</IonSelectOption>
                    </IonSelect>
                    {/*<IonInput value={locked.toString()} onIonChange={e => setLocked(e.detail.value || '')}/>*/}
                </IonItem>
                <IonItem>
                    <IonLabel>BatteryLevel:</IonLabel>
                    <IonInput value={batteryLevel} onIonChange={e => setBatteryLevel(Number(e.detail.value) || 0)}/>
                </IonItem>
                <IonItem>
                    <IonText>{latitude}</IonText>
                </IonItem>
                <IonItem>
                    <IonText>{longitude}</IonText>
                </IonItem>
                <IonItem>
                    <IonImg style={{width: "300px", height: "300px", margin: "0 auto"}} alt={"NO PHOTO"}
                            src={photoPath}/>
                </IonItem>
                <IonItem>
                    <MyMap lat={latitude} lng={longitude} onMapClick={setMapPosition} onMarkerClick={() => {
                    }}/>
                </IonItem>
                <IonCardContent>
                    <IonModal
                        isOpen={showModal}
                        enterAnimation={enterAnimation}
                        leaveAnimation={leaveAnimation}>
                        <div slot='center'>
                            <IonText>Other useful info</IonText>
                        </div>
                        <IonButton onClick={() => setShowModal(false)}>Close info window</IonButton>
                    </IonModal>
                    <IonButton onClick={() => setShowModal(true)}>open</IonButton>
                </IonCardContent>
                <IonFab horizontal="end">
                    <IonFabButton size="small" color="danger"
                                  onClick={() => {
                                      const photoTaken = takePhoto();
                                      photoTaken.then((data) => {
                                          console.log(data)
                                          let filepath = data.filepath;
                                          setPhotoFile(filepath)
                                          setPhotoPath(data.webviewPath!);
                                      });
                                  }}>
                        <IonIcon icon={camera}/>
                    </IonFabButton>
                </IonFab>
                <IonLoading isOpen={saving}/>
                {savingError && (
                    <div>{savingError.message || 'Failed to save item'}</div>
                )}
                {
                    offlineText
                }
            </IonContent>
        </IonPage>
    );
};

export default ScooterEdit;
