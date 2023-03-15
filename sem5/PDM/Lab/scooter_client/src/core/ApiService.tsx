import axios from 'axios';
import {authConfig, baseUrl, getLogger, withLogs} from './index';
import {Plugins} from "@capacitor/core";
import {ScooterProps} from "../components/scooter/Scooter";

// noinspection HttpUrlsUsage
const scooterUrl = `http://${baseUrl}/api/scooter`;
const scootersUrl = `http://${baseUrl}/api/scooter`;

export const getScooters: (token: string, searchText: string) => Promise<ScooterProps[]> = (token, searchText) => {
   console.log('${searchText}');
   console.log("search text " + searchText);
   return withLogs(axios.get(`${scootersUrl}/${searchText}`, authConfig(token)), 'getScooters');
}

// @ts-ignore
export const createScooter: (token: string, scooter: ScooterProps, networkStatus: any, present: any) => Promise<ScooterProps[]> = (token, scooter, networkStatus, present) => {
    function offlineActionGenerator() {
        return new Promise<ScooterProps[]>(async (resolve) => {
            const {Storage} = Plugins;
            present("Couldn't send data to the server, caching it locally", 3000)
            await Storage.set({
                key: `sav-${scooter._id}`,
                value: JSON.stringify({
                    token,
                    scooter
                })
            })
            // @ts-ignore
            resolve(scooter)
        })
    }
    if (networkStatus.connected) {
        return withLogs(axios.post(scooterUrl, scooter, authConfig(token)), 'createScooter')
            .catch(() => {
            return offlineActionGenerator()
        });
    }
    return offlineActionGenerator()
}


// @ts-ignore
export const updateScooter: (token: string, scooter: ScooterProps, networkStatus: any, present: any) => Promise<ScooterProps[]> = (token, scooter, networkStatus, present) => {
    function offlineActionGenerator() {
        return new Promise<ScooterProps[]>(async (resolve) => {
            const {Storage} = Plugins;
            present("Couldn't send data to the server, caching it locally", 3000)
            await Storage.set({
                key: `upd-${scooter._id}`,
                value: JSON.stringify({
                    token,
                    scooter
                })
            })
            // @ts-ignore
            resolve(scooter)
        })
    }
    if (networkStatus.connected)
        return withLogs(axios.put(`${scooterUrl}/${scooter._id}`, scooter, authConfig(token)), 'updateScooter').catch(() => {
            return offlineActionGenerator()
        });
    return offlineActionGenerator()
}

interface MessageData {
    type: string;
    payload: {
        scooter: ScooterProps;
    };
}

const log = getLogger('ws');

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
    const webSocket = new WebSocket(`ws://${baseUrl}`)
    webSocket.onopen = () => {
        log('web socket onopen');
        webSocket.send(JSON.stringify({ type: 'authorization', payload: { token } }));
    };
    webSocket.onclose = () => {
        log('web socket onclose');
    };
    webSocket.onerror = error => {
        log('web socket onerror', error);
    };
    webSocket.onmessage = messageEvent => {
        log('web socket onmessage');
        onMessage(JSON.parse(messageEvent.data));
    };
    return () => {
        webSocket.close();
    }
}
