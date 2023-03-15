import React, {useCallback, useContext, useEffect, useReducer} from "react";
import PropTypes from "prop-types";
import {createScooter, getScooters, newWebSocket, updateScooter} from "../../core/ApiService";
import {useNetwork} from "../network/useNetwork";
import {useIonToast} from "@ionic/react";
import {Plugins} from "@capacitor/core";
import {ScooterProps} from "./Scooter";
import {AuthContext} from "../auth/AuthProvider";

interface ActionProps {
    type: string,
    payload?: any,
}

export interface ScootersState {
    scooters?: ScooterProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveScooter?: SaveScooterFunction,
    searchText: string,
    setSearchText?: SetSearchTextFunction
}

export type SaveScooterFunction = (scooter: ScooterProps) => Promise<any>;
export type SetSearchTextFunction = (text: string) => void;

export const initialState: ScootersState = {
    fetching: false,
    saving: false,
    searchText: ''
};

export const ScooterContext = React.createContext<ScootersState>(initialState);

const FETCH_SCOOTERS_STARTED = 'FETCH_SCOOTERS_STARTED';
const FETCH_SCOOTERS_SUCCEEDED = 'FETCH_SCOOTERS_SUCCEEDED';
const FETCH_SCOOTERS_FAILED = 'FETCH_SCOOTERS_FAILED';
const SAVE_SCOOTER_STARTED = 'SAVE_SCOOTER_STARTED';
const SAVE_SCOOTER_SUCCEEDED = 'SAVE_SCOOTER_SUCCEEDED';
const SAVE_SCOOTER_FAILED = 'SAVE_SCOOTER_FAILED';
const SET_SEARCH_TEXT = 'SET_SEARCH_TEXT';

const reducer: (state: ScootersState, action: ActionProps) => ScootersState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_SCOOTERS_STARTED:
                return { ...state, fetching: true, fetchingError: null };
            case FETCH_SCOOTERS_SUCCEEDED:
                return { ...state, scooters: payload.scooters, fetching: false };
            case FETCH_SCOOTERS_FAILED:
                return { ...state, fetchingError: payload.error, fetching: false };
            case SAVE_SCOOTER_STARTED:
                return { ...state, savingError: null, saving: true };
            case SAVE_SCOOTER_SUCCEEDED:
                const scooters = [...(state.scooters || [])];
                const scooter = payload.scooter;
                let index = scooters.findIndex(it => it._id === scooter._id);
                if (index === -1) {
                    scooters.splice(0, 0, scooter);
                } else {
                    scooters[index] = scooter;
                }
                return { ...state, scooters: scooters, saving: false };
            case SAVE_SCOOTER_FAILED:
                return { ...state, savingError: payload.error, saving: false };
            case SET_SEARCH_TEXT:
                return {...state, searchText: payload.text}
            default:
                return state;
        }
    };

interface ScooterProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const ScooterProvider: React.FC<ScooterProviderProps> = ({children}) => {
    const {token} = useContext(AuthContext);
    const [state, dispatch] = useReducer(reducer, initialState);
    const {scooters, fetching, fetchingError, saving, savingError, searchText} = state;
    const {networkStatus} = useNetwork()
    const [present] = useIonToast();
    useEffect(getItemsEffect, [token]);
    useEffect(executePendingOperations, [networkStatus.connected, token])
    useEffect(webSocketsEffect, [token]);
    const saveScooter = useCallback<SaveScooterFunction>(saveScooterCallback, [networkStatus, token, present]);
    const setSearchText = useCallback<SetSearchTextFunction>(setSearchTextCallback, [searchText])
    const value = {scooters, fetching, fetchingError, saving, savingError, saveScooter, searchText, setSearchText};
    return (
        <ScooterContext.Provider value={value}>
            {children}
        </ScooterContext.Provider>
    );

    async function fetchScooters() {
                if (!token?.trim()) {
                    return;
                }
                try {
                    console.log("in try to search scooters with " + searchText)
                    dispatch({type: FETCH_SCOOTERS_STARTED});
                    const scooters = await getScooters(token, searchText);
                    dispatch({type: FETCH_SCOOTERS_SUCCEEDED, payload: {scooters: scooters}});
                } catch (error) {
                    dispatch({type: FETCH_SCOOTERS_FAILED, payload: {error}});
                }
            }


    function getItemsEffect() {
        fetchScooters().then(_ => {
        });
    }

    async function setSearchTextCallback(text: string) {
        dispatch({type: SET_SEARCH_TEXT, payload: {text: text}})
        await fetchScooters()
    }

    async function saveScooterCallback(scooter: ScooterProps) {
        try {
            dispatch({type: SAVE_SCOOTER_STARTED});
            const savedScooter = await (scooter._id ? updateScooter(token, scooter, networkStatus, present) : createScooter(token, scooter, networkStatus, present));
            dispatch({type: SAVE_SCOOTER_SUCCEEDED, payload: {scooter: savedScooter}});
        } catch (error) {
            dispatch({type: SAVE_SCOOTER_FAILED, payload: {error}});
        }
    }

    function executePendingOperations() {
        async function helperMethod() {
            if (networkStatus.connected && token?.trim()) {
                console.log("executing pending operations")
                const {Storage} = Plugins;
                const {keys} = await Storage.keys();
                for (const key of keys) {
                    if (key.startsWith("sav-")) {
                        console.log("here")
                        const value = JSON.parse((await Storage.get({key: key})).value!!)
                        await createScooter(value.token, value.scooter, networkStatus, present)
                        await Storage.remove({key: key})
                    } else if (key.startsWith("upd-")) {
                        const value = JSON.parse((await Storage.get({key: key})).value!!)
                        await updateScooter(value.token, value.scooter, networkStatus, present)
                        await Storage.remove({key: key})
                    }
                }
            }
        }
        // noinspection JSIgnoredPromiseFromCall
        helperMethod()
    }

    function webSocketsEffect() {
        let canceled = false;
        let closeWebSocket: () => void;
        if (token?.trim()) {
            closeWebSocket = newWebSocket(token, message => {
                if (canceled) {
                    return;
                }
                const {type, payload: scooter} = message;
                if (type === 'created' || type === 'updated') {
                    dispatch({type: SAVE_SCOOTER_SUCCEEDED, payload: {scooter: scooter}});
                }
            });
            return () => {
                canceled = true;
                closeWebSocket?.();
            }
        }
    }
}