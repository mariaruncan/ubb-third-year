import axios from 'axios';
import {authConfig, baseUrl, getLogger, withLogs} from '../core';
import {ScooterProps} from './ScooterProps';

const scooterUrl = `http://${baseUrl}/api/item`;

export const getScooters: (token: string) => Promise<ScooterProps[]> = token => {
    return withLogs(axios.get(scooterUrl, authConfig(token)), 'getScooters');
}

export const getScootersPaginated: (token: string, page: number) => Promise<ScooterProps[]> = (token, page) => {
    return withLogs(axios.get(`${scooterUrl}/page/${page}`, authConfig(token)), 'getScootersPaginated');
}

export const getScootersFilteredPaginated: (token: string, locked: boolean, page: number) => Promise<ScooterProps[]> = (token, locked, page) => {
    return withLogs(axios.get(`${scooterUrl}/${locked}/${page}`, authConfig(token)), 'getScootersFiltered');
}

export const getScootersFiltered: (token: string, locked: boolean) => Promise<ScooterProps[]> = (token, locked) => {
    return withLogs(axios.get(`${scooterUrl}/${locked}/0`, authConfig(token)), 'getScootersFiltered');
}

export const createScooter: (token: string, item: ScooterProps) => Promise<ScooterProps[]> = (token, scooter) => {
    console.log('createScooter ', scooter)
    return withLogs(axios.post(scooterUrl, scooter, authConfig(token)), 'createScooter');
}

export const updateScooter: (token: string, scooter: ScooterProps) => Promise<ScooterProps[]> = (token, scooter) => {
    return withLogs(axios.put(`${scooterUrl}/${scooter._id}`, scooter, authConfig(token)), 'updateScooter');
}

interface MessageData {
    type: string;
    payload: ScooterProps;
}

const log = getLogger('ws');

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
