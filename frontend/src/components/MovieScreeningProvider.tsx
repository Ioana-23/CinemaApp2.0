import React, {useCallback, useEffect, useReducer} from 'react';
import PropTypes from 'prop-types';
import {MovieScreeningProps} from './MovieScreeningProps';
import {getItems, Response} from "./MovieScreeningApi.tsx";

type SaveMovieFn = (movie_screening: MovieScreeningProps) => Promise<any>;
type PaginationFunction = (movie_screening: MovieScreeningProps) => Promise<MovieScreeningProps[]>;
type MoviesFn = (movie_screening: MovieScreeningProps[]) => void;

export const NUMBER_OF_MOVIES_PER_PAGE = 5;

export interface MovieScreeningState {
    items?: MovieScreeningProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveItem?: SaveMovieFn,
    paginationFunction?: PaginationFunction,
    setItems?: MoviesFn;
}

interface ActionProps {
    type: string,
    payload?: any,
}

export const initialStateItems: MovieScreeningState = {
    fetching: false,
    saving: false,
};

export const MovieContext = React.createContext<MovieScreeningState>(initialStateItems);

const FETCH_ITEMS_STARTED = 'FETCH_ITEMS_STARTED';
const FETCH_ITEMS_SUCCEEDED = 'FETCH_ITEMS_SUCCEEDED';
const FETCH_ITEMS_FAILED = 'FETCH_ITEMS_FAILED';
const SAVE_ITEM_STARTED = 'SAVE_ITEM_STARTED';
const SAVE_ITEM_SUCCEEDED = 'SAVE_ITEM_SUCCEEDED';
const SAVE_ITEM_FAILED = 'SAVE_ITEM_FAILED';
const FETCH_REVIEWS_SUCCEEDED = 'SAVE_REVIEWS_SUCCEEDED';
const FETCH_REVIEWS_FAILED = 'FETCH_REVIEWS_FAILED';

const reducer: (state: MovieScreeningState, action: ActionProps) => MovieScreeningState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_ITEMS_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_ITEMS_SUCCEEDED:
                return {...state, items: payload.items.data.responseObject, fetching: false};
            case FETCH_ITEMS_FAILED:
                return {...state, fetchingError: payload.error, fetching: false};
            case SAVE_ITEM_STARTED:
                return {...state, savingError: null, saving: true};
            // case SAVE_ITEM_SUCCEEDED: {
            //     const items = [...(state.items || [])];
            //     const item = payload.item;
            //     const index = items.findIndex(it => it._uuids === item._uuids);
            //     if (index === -1) {
            //         items.splice(0, 0, item);
            //     } else {
            //         items[index] = item;
            //     }
            //     return {...state, items, saving: false};
            // }
            case SAVE_ITEM_FAILED:
                return {...state, savingError: payload.error, saving: false};
            case FETCH_REVIEWS_SUCCEEDED:
                return {...state, reviews: payload.reviews, fetching: false};
            case FETCH_REVIEWS_FAILED:
                return {...state, fetching: false, fetchingError: payload.error};
            default:
                return state;
        }
    };

interface MovieScreeningProviderProps {
    children: PropTypes.ReactNodeLike,
    dateToFilterBy: Date
}

export const MovieScreeningProvider: React.FC<MovieScreeningProviderProps> = ({children, dateToFilterBy}) => {
    const [state, dispatch] = useReducer(reducer, initialStateItems);
    const {items, fetching, fetchingError, saving, savingError} = state;
    useEffect(getMoviesEffect, [dateToFilterBy]);
    const setItems = useCallback<MoviesFn>(setItemsCallback, []);
    const value = {items, fetching, fetchingError, saving, savingError, setItems};
    return (
        <MovieContext.Provider value={value}>
            {children}
        </MovieContext.Provider>
    );

    async function setItemsCallback(itemsAux: MovieScreeningProps[]) {
        await setItems();
        function setItems()
        {
            dispatch({type: FETCH_ITEMS_SUCCEEDED, payload: {items: itemsAux}});
        }
    }

    function getMoviesEffect() {
        let canceled = false;
        fetchItemsConnected();
        return () => {
            canceled = true;
        }

        async function fetchItemsConnected() {
            try {
                dispatch({type: FETCH_ITEMS_STARTED});
                const stringDate = `${dateToFilterBy.getDate()}-${dateToFilterBy.getMonth()+1}-${dateToFilterBy.getFullYear()}`;
                const moviesAux = await getItems(stringDate);
                if (!canceled) {
                    dispatch({type: FETCH_ITEMS_SUCCEEDED, payload: {items: moviesAux}});
                }
            } catch (error) {
                dispatch({type: FETCH_ITEMS_FAILED, payload: {error}});
            }
        }
    }
};
