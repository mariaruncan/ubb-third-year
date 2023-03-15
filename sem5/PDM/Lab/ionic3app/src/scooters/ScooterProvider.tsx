import React, {useCallback, useContext, useEffect, useReducer, useState} from 'react';
import PropTypes from 'prop-types';
import {getLogger} from '../core';
import {ScooterProps} from './ScooterProps';
import {createScooter, getScooters, getScootersFiltered, getScootersPaginated, newWebSocket, updateScooter} from './scooterApi';
import {AuthContext} from '../auth';
import {save} from "ionicons/icons";
import { Preferences } from '@capacitor/preferences';
// import {useNetwork} from "../network/useNetwork";
import scooter from "./Scooter";

const log = getLogger('ScooterProvider');

type SaveScooterFn = (item: ScooterProps) => Promise<any>;
type FetchFN = () => void;
type FetchFilterFN = (language: string) => void;

export interface ScootersState {
    scooters?: ScooterProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    resetLogout?: FetchFN,
    fetchScooters?: FetchFilterFN,
    fetchFilteredScooters?: FetchFilterFN,
    savingError?: Error | null,
    saveScooter?: SaveScooterFn,
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: ScootersState = {
    fetching: false,
    saving: false,
};

const FETCH_ITEMS_STARTED = 'FETCH_ITEMS_STARTED';
const FETCH_ITEMS_SUCCEEDED = 'FETCH_ITEMS_SUCCEEDED';
const FETCH_ITEMS_FAILED = 'FETCH_ITEMS_FAILED';
const SAVE_ITEM_STARTED = 'SAVE_ITEM_STARTED';
const SAVE_ITEM_SUCCEEDED = 'SAVE_ITEM_SUCCEEDED';
const SAVE_ITEM_FAILED = 'SAVE_ITEM_FAILED';
const PAGE_SIZE = 10;

const reducer: (state: ScootersState, action: ActionProps) => ScootersState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_ITEMS_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_ITEMS_SUCCEEDED:
                return {...state, scooters: payload.items, fetching: false};
            case FETCH_ITEMS_FAILED:
                return {...state, fetchingError: payload.error, fetching: false};
            case SAVE_ITEM_STARTED:
                return {...state, savingError: null, saving: true};
            case SAVE_ITEM_SUCCEEDED:
                const scooters = [...(state.scooters || [])];
                const item = payload.item;
                const index = scooters.findIndex(it => it._id === item._id);
                if (index === -1) {
                    scooters.splice(0, 0, item);
                } else {
                    scooters[index] = item;
                }
                return {...state, scooters, saving: false};
            case SAVE_ITEM_FAILED:
                return {...state, savingError: payload.error, saving: false};
            default:
                return state;
        }
    };


export const ScooterContext = React.createContext<ScootersState>(initialState);

interface ItemProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const ScooterProvider: React.FC<ItemProviderProps> = ({children}) => {
    const [pageNumber, setPageNumber] = useState(0);
    const [lastLanguage, setLastLanguage] = useState("default");
    // const {Storage} = Plugins;
    const {token} = useContext(AuthContext);
    const [state, dispatch] = useReducer(reducer, initialState);
    const {scooters, fetching, fetchingError, saving, savingError} = state;
    const saveScooter = useCallback<SaveScooterFn>(saveItemCallback, [token]);
    const value = {scooters, fetching, fetchingError, saving, savingError, saveScooter, fetchScooters};
    // const { networkStatus } = useNetwork();

    // useEffect(() => {
    //     const onUseEffect = async () => {
    //         const res = await Storage.get({key: 'scooters'});
    //         if (!res.value) {
    //             fetchScooters("default");
    //         } else {
    //            getItemsFromStorage();
    //         }
    //     }
    //     onUseEffect().then();
    // }, [])

    useEffect(() => {
        fetchScooters("default");
    }, [])

    // useEffect(() => {
    //     if (networkStatus.connected) {
    //         saveItemsFromStorage().then();
    //     }
    //
    //     async function saveItemsFromStorage() {
    //         const res = await Storage.get({ key: 'temporary-scooters' });
    //         if (res.value) {
    //             const temporaryScooters = JSON.parse(res.value);
    //             log('saveItemsFromStorage' + JSON.stringify(temporaryScooters))
    //             temporaryScooters.forEach(function(scooter: ScooterProps) {
    //                 saveItemCallback(scooter)
    //             });
    //
    //             await Storage.remove({ key: 'temporary-scooters' });
    //         }
    //     }
    // }, [networkStatus.connected])

    useEffect(wsEffect, [token]);
    log('returns ' + pageNumber);

    function getItemsFromStorage() {
        getScootersFromStorage().then();
        async function getScootersFromStorage() {
            try {
                dispatch({type: FETCH_ITEMS_STARTED});
                const res = await Preferences.get({key: 'scooters'});
                if (res.value) {
                    const items = JSON.parse(res.value);
                    log(items)
                    dispatch({type: FETCH_ITEMS_SUCCEEDED, payload: {items}});
                }
            } catch (error) {
                log('fetchItems failed');
                dispatch({type: FETCH_ITEMS_FAILED, payload: {error}});
            }
        }
    }

    async function saveScooterInStorage(editedScooter: ScooterProps) {
        const res = await Preferences.get({ key: 'temporary-scooters' });
        if (res.value) {
            let temporaryScooters = JSON.parse(res.value);
            temporaryScooters.push(editedScooter)
            await Preferences.set({
                key: 'temporary-scooters',
                value: JSON.stringify(temporaryScooters)
            });
            log('saveScooterInStorage ' + JSON.stringify(temporaryScooters))
        }
        else {
            await Preferences.set({
                key: 'temporary-scooters',
                value: JSON.stringify([editedScooter])
            });
        }
    }

    async function saveItemCallback(item: ScooterProps) {
        // if (!networkStatus.connected) {
        //         log('saveItemCallback not connected')
        //     saveScooterInStorage(item).then();
        //     return;
        // }
        try {
            log('saveItem started');
            dispatch({type: SAVE_ITEM_STARTED});
            const resToken = await Preferences.get({key: 'token'});
            let tok =''
            if (resToken.value) {
                tok = resToken.value
            }
            const savedItem = await (item._id ? updateScooter(tok, item) : createScooter(tok, item));
            console.log('saveItem   ', savedItem)
            log('saveItem succeeded');
            dispatch({type: SAVE_ITEM_SUCCEEDED, payload: {item: savedItem}});
        } catch (error) {
            log('saveItem failed');
            dispatch({type: SAVE_ITEM_FAILED, payload: {error}});
        }
    }

    function wsEffect() {
        let canceled = false;
        log('wsEffect - connecting');
        let closeWebSocket: () => void;
        if (token?.trim()) {
            closeWebSocket = newWebSocket(token, message => {
                if (canceled) {
                    return;
                }
                const {type, payload: item} = message;
                log(`ws message, item ${type}`);
                if (type === 'created' || type === 'updated') {
                    dispatch({type: SAVE_ITEM_SUCCEEDED, payload: {item}});
                }
            });
        }
        return () => {
            log('wsEffect - disconnecting');
            canceled = true;
            closeWebSocket?.();
        }
    }

    function fetchFilteredScooters(language: string) {
        log('fetchFilteredScooters scooters ' + language)
        let canceled = false;
        fetchItems().then();
        return () => {
            canceled = true;
        }

        async function fetchItems() {
            log('fetchFilteredScooters')
            const resToken = await Preferences.get({key: 'token'});
            if (!resToken.value) {
                return;
            }
            try {
                log('fetchFilteredScooters started paginated');
                dispatch({type: FETCH_ITEMS_STARTED});

                if (language != lastLanguage) {
                    setPageNumber(0);
                    log('set page number', pageNumber)
                    await Preferences.remove({key: 'scooters'});
                }
                setLastLanguage(language);
                let tok = "";
                const resToken = await Preferences.get({key: 'token'});
                if (resToken.value) {
                    tok = resToken.value
                }
                const newItems = await getScootersFiltered(tok, false);
                const res = await Preferences.get({key: 'scooters'});
                log(newItems)
                let items;
                if (res.value) {
                    const resJSON = JSON.parse(res.value);
                    items = resJSON.concat(newItems);
                } else
                    items = newItems

                log('fetchFilteredScooters succeeded paginated');
                if (!canceled) {
                    dispatch({type: FETCH_ITEMS_SUCCEEDED, payload: {items}});
                    await Preferences.set({key: 'scooters', value: JSON.stringify(items)});
                }
            } catch (error) {
                log('fetchFilteredScooters failed paginated');
                dispatch({type: FETCH_ITEMS_FAILED, payload: {error}});
            }
        }
    }

    function fetchScooters(language: string) {
        log('fetchScooters ' + language)
        if (language !== 'default') {
            fetchFilteredScooters(language);
            return;
        }
        let canceled = false;
        fetchItems();
        return () => {
            canceled = true;
        }

        async function fetchItems() {
            const resToken = await Preferences.get({key: 'token'});
            if (!resToken.value) {
                return;
            }
            try {
                log('fetchItems started paginated ', language, lastLanguage);
                dispatch({type: FETCH_ITEMS_STARTED});

                if (language != lastLanguage) {
                    setPageNumber(0);
                    log('set page number', pageNumber)
                    await Preferences.remove({key: 'scooters'});
                }
                setLastLanguage("default");
                let tok = "";
                const resToken = await Preferences.get({key: 'token'});
                if (resToken.value) {
                    tok = resToken.value
                }

                const newItems = await getScootersPaginated(tok, pageNumber);
                const res = await Preferences.get({key: 'scooters'});

                let items = [];
                if (res.value) {
                    const storageItems = JSON.parse(res.value);
                    //items = storageItems.concat(newItems);
                    items = newItems
                } else
                    items = newItems
                // setPageNumber(pageNumber + PAGE_SIZE)
                log('page ' + pageNumber)
                log('fetchItems succeeded paginated');
                if (!canceled) {
                    dispatch({type: FETCH_ITEMS_SUCCEEDED, payload: {items}});
                    log('mov ' + JSON.stringify(items))
                    await Preferences.set({key: 'scooters', value: JSON.stringify(items)});
                }
            } catch (error) {
                log('fetchItems failed paginated');
                dispatch({type: FETCH_ITEMS_FAILED, payload: {error}});
            }
        }
    }

    return (
        <ScooterContext.Provider value={value}>
            {children}
        </ScooterContext.Provider>
    );
};