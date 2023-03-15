import React, {useContext, useState} from 'react';
import {RouteComponentProps} from 'react-router';
import {
    IonButton,
    IonButtons,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon, IonInfiniteScroll, IonInfiniteScrollContent, IonList, IonLoading, IonPage, IonSearchbar,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import {add} from 'ionicons/icons';
import Scooter, {ScooterProps} from "./Scooter";
import NetworkStatus from "../network/NetworkStatus";
import {ScooterContext} from "./ScooterProvider";
import {AuthContext} from "../auth/AuthProvider";

const indicesPresented = 3;

const ScooterList: React.FC<RouteComponentProps> = ({history}) => {
    const {scooters, fetching,fetchingError, setSearchText, searchText} = useContext(ScooterContext);
    const {logout} = useContext(AuthContext);
    const [index, setIndex] = useState<number>(0);
    const [items, setItems] = useState<ScooterProps[]>([]);
    const [hasMore, setHasMore] = useState<boolean>(true);
    const [hasFetched, setHasFetched] = useState<boolean>(false);


    if (!hasFetched) {
        if (scooters) {
            fetchData()
            setHasFetched(true);
        }
    }

    function fetchData() {
        if (scooters) {
            console.log("fetching new scooter data")
            const newIndex = Math.min(index + indicesPresented, scooters.length);
            if (newIndex >= scooters.length)
                setHasMore(false);
            else setHasMore(true);
            setItems(scooters.slice(0, newIndex));
            setIndex(newIndex)
        }
    }

    async function searchNext($event: CustomEvent<void>) {
        await fetchData();
        await ($event.target as HTMLIonInfiniteScrollElement).complete();
    }

    function handleLogout() {
        logout?.()
        history.push('/login')
    }

    function handleTextChange(e: any) {
        setItems([])
        setIndex(0)
        setHasFetched(false)
        setHasMore(true)
        console.log(e.detail.value)
        setSearchText?.(e.detail.value!!)
    }


    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>My Scooters</IonTitle>
                    <NetworkStatus/>
                    <IonButtons slot="end">
                        <IonButton onClick={handleLogout}>
                            Logout
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                <IonSearchbar value={searchText} onIonChange={e => handleTextChange(e)}/>
                <IonList>
                    {items.map(({_id, number, locked, batteryLevel, updatedAt}) =>
                        <Scooter key={_id} _id={_id} number={number} locked={locked} batteryLevel={batteryLevel}
                                  onEdit={id => history.push(`/scooter/${id}`)}/>)}
                </IonList>
                <IonInfiniteScroll threshold="0px" disabled={!hasMore}
                                   onIonInfinite={(e: CustomEvent<void>) => searchNext(e)}>
                    <IonInfiniteScrollContent
                        loadingText="Loading more scooters...">
                    </IonInfiniteScrollContent>
                </IonInfiniteScroll>
                {fetchingError && (
                    <div>{fetchingError.message || 'Failed to fetch items'}</div>
                )}
                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/scooter')}>
                        <IonIcon icon={add}/>
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default ScooterList;
