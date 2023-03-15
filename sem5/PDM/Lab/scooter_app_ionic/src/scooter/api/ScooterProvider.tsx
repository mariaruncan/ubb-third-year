import {getLogger} from "../../core";
import {Scooter} from "../model/Scooter";
import React, {useCallback, useContext, useEffect, useReducer} from "react";
import PropTypes from "prop-types";
import {createScooter, getScooters, newWebSocket, updateScooter} from "./ScooterApi";
import {AuthContext} from "../../auth/api/AuthProvider";


const log = getLogger('ScooterProvider');

type SaveScooterFn = (item: Scooter) => Promise<any>;

export interface ScootersListState {
    scooters?: Scooter[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveScooter?: SaveScooterFn,
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: ScootersListState = {
    fetching: false,
    saving: false,
}

const FETCH_SCOOTERS_STARTED = 'FETCH_SCOOTERS_STARTED';
const FETCH_SCOOTERS_SUCCEEDED = 'FETCH_SCOOTERS_SUCCEEDED';
const FETCH_SCOOTERS_FAILED = 'FETCH_SCOOTERS_FAILED';

const SAVE_SCOOTER_STARTED = 'SAVE_SCOOTER_STARTED';
const SAVE_SCOOTER_SUCCEEDED = 'SAVE_SCOOTER_SUCCEEDED';
const SAVE_SCOOTER_FAILED = 'SAVE_SCOOTER_FAILED';

const reducer: (state: ScootersListState, action: ActionProps) => ScootersListState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_SCOOTERS_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_SCOOTERS_SUCCEEDED:
                return {...state, scooters: payload.scooters, fetching: false};
            case FETCH_SCOOTERS_FAILED:
                return {...state, fetchingError: payload.error, fetching: false};
            case SAVE_SCOOTER_STARTED:
                return {...state, savingError: null, saving: true};
            case SAVE_SCOOTER_SUCCEEDED:
                const scooters = [...(state.scooters || [])];
                const scooter = payload.scooter;
                const index = scooters.findIndex(b => b._id === scooter.id);
                if (index === -1) {
                    scooters.splice(0, 0, scooter);
                } else {
                    scooters[index] = scooter;
                }
                return {...state, scooters: scooters, saving: false};
            case SAVE_SCOOTER_FAILED:
                return {...state, savingError: payload.error, saving: false};
            default:
                return state;
        }
    };

export const ScooterContext = React.createContext<ScootersListState>(initialState);

interface ScooterProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const ScooterProvider: React.FC<ScooterProviderProps> = ({children}) => {
    const {token} = useContext(AuthContext)
    const [state, dispatch] = useReducer(reducer, initialState);
    const {scooters, fetching, fetchingError, saving, savingError} = state;
    useEffect(getScootersEffect, [token]);
    useEffect(wsEffect, [token]);

    const saveScooter = useCallback<SaveScooterFn>(saveScooterCallback, [token]);
    const value = {scooters, fetching, fetchingError, saving, savingError, saveScooter};
    log('returns');

    return (
        <ScooterContext.Provider value={value}>
            {children}
        </ScooterContext.Provider>
    );

    function getScootersEffect() {
        let canceled = false;
        fetchScooters().then();
        return () => {
            canceled = true;
        }

        async function fetchScooters() {
            if(!token?.trim()) {
                return;
            }
            try {
                log('fetchScooters started');
                dispatch({type: FETCH_SCOOTERS_STARTED});
                const scooters = await getScooters(token);
                log('fetchScooters succeeded');
                if (!canceled) {
                    dispatch({type: FETCH_SCOOTERS_SUCCEEDED, payload: {scooters: scooters}});
                }
            } catch (error) {
                log('fetchScooters failed');
                dispatch({type: FETCH_SCOOTERS_FAILED, payload: {error}});
            }
        }
    }

    async function saveScooterCallback(scooter: Scooter) {
        try {
            log('saveScooter started');
            dispatch({type: SAVE_SCOOTER_STARTED});
            const savedScooter = await (scooter._id ? updateScooter(token, scooter) : createScooter(token, scooter));
            log('saveScooter succeeded');
            dispatch({type: SAVE_SCOOTER_SUCCEEDED, payload: {scooter: savedScooter}});
        } catch (error) {
            log('saveScooter failed');
            dispatch({type: SAVE_SCOOTER_FAILED, payload: {error}});
        }
    }

    function wsEffect() {
        let canceled = false;
        log('wsEffect - connecting');
        const closeWebSocket = newWebSocket(token, message => {
            if (canceled) {
                return;
            }
            const {type, payload: scooter} = message;
            log(`ws message, item ${type}`);
            if (type === 'created' || type === 'updated') {
                dispatch({type: SAVE_SCOOTER_SUCCEEDED, payload: {scooter}});
            }
        });
        return () => {
            log('wsEffect - disconnecting');
            canceled = true;
            closeWebSocket();
        }
    }
}