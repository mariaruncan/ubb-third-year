import {authConfig, baseUrl, getLogger, withLogs} from "../../core";
import {Scooter} from "../model/Scooter";
import axios from "axios";

const scooterUrl = `http://${baseUrl}/api/scooter`;

export const getScooters: (token: string) => Promise<Scooter[]> = token => {
    return withLogs(axios.get(scooterUrl, authConfig(token)), "getScooters");
}

export const createScooter: (token: string, scooter: Scooter) => Promise<Scooter[]> = (token, scooter) => {
    return withLogs(axios.post(scooterUrl, scooter, authConfig(token)), 'createItem');
}

export const updateScooter: (token: string, scooter: Scooter) => Promise<Scooter[]> = (token, scooter) => {
    return withLogs(axios.put(`${scooterUrl}/${scooter._id}`, scooter, authConfig(token)), 'updateItem');
}

interface MessageData {
    type: string;
    payload: Scooter;
}

const log = getLogger('WS: ');

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
    const ws = new WebSocket(`ws://${baseUrl}`);
    ws.onopen = () => {
        log('web socket onopen');
        ws.send(JSON.stringify({type: 'authorization', payload: {token}}));
    };
    ws.onclose = () => {
        log('web socket onclose');
    };
    ws.onerror = error => {
        log('web socket onerror', error);
    };
    ws.onmessage = messageEvent => {
        log('web socket onmessage');
        onMessage(JSON.parse(messageEvent.data));
    };
    return () => {
        ws.close();
    }
}