import React, {useCallback, useEffect, useReducer} from 'react';
import PropTypes from 'prop-types';
import {MovieScreeningProps} from './MovieScreeningProps';
import {getMovieInfo, getMovieScreening} from "./MovieScreeningApi.tsx";

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

const FETCH_MOVIE_SCREENING_STARTED = 'FETCH_MOVIE_SCREENING_STARTED';
const FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO = 'FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO';
const FETCH_MOVIE_INFO_STARTED = 'FETCH_MOVIE_INFO_STARTED';
const FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING = 'FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING';
const FETCH_MOVIE_INFO_FINISHED = 'FETCH_MOVIE_INFO_FINISHED';
const FETCH_MOVIE_SCREENING_FINISHED = 'FETCH_MOVIE_SCREENING_FINISHED';

const reducer: (state: MovieScreeningState, action: ActionProps) => MovieScreeningState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_MOVIE_SCREENING_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO:
                return {...state, items: payload.items.data.responseObject.movieScreeningDTOS, fetching: false};
            case FETCH_MOVIE_INFO_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_MOVIE_INFO_FINISHED:
                return {...state, items: payload.items, fetching: false};
            case FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING: {
                const items = [...(state.items || [])];
                const item = payload.item.data.responseObject
                let index = items.findIndex(it => it.movie_uuid === item.uuid);
                while(index != -1 && index < items.length)
                {
                    if(items[index].movie_uuid === item.uuid)
                    {
                        items[index].movie = item;
                    }
                    index++;
                }
                return { ...state, items, fetching: true, fetchingError: null };}
            default:
                return state;
        }
    };

interface MovieScreeningProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const MovieScreeningProvider: React.FC<MovieScreeningProviderProps> = ({children}) => {
    const [state, dispatch] = useReducer(reducer, initialStateItems);
    const {items, fetching, fetchingError, saving, savingError} = state;
    useEffect(getMoviesEffect, []);
    useEffect(getMovieInformation, [items])
    // const setItems = useCallback<MoviesFn>(setItemsCallback, []);
    const value = {items, fetching, fetchingError, saving, savingError};
    return (
        <MovieContext.Provider value={value}>
            {children}
        </MovieContext.Provider>
    );

    function getMovieInformation() {
        let canceled1 = false;
        fetchMovieInformation();
        return () => {
            canceled1 = true;
        }

        async function fetchMovieInformation() {
            try {
                dispatch({type: FETCH_MOVIE_INFO_STARTED});
                // const stringDate = `${dateToFilterBy.getDate()}-${dateToFilterBy.getMonth()+1}-${dateToFilterBy.getFullYear()}`;
                if (items) {
                    for (let i = 0; i < items.length; i++) {
                        if(!items[i].movie) {
                            const uuid: number = items[i].movie_uuid;
                            const movieInfo: Response = await getMovieInfo(uuid);
                            dispatch({type: FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING, payload: {item: movieInfo}});
                        }
                    }
                }
                if (!canceled1) {
                    dispatch({type: FETCH_MOVIE_INFO_FINISHED, payload: {items: items}});
                }
            } catch (error) {
                dispatch({type: FETCH_MOVIE_INFO_FINISHED, payload: {error}});
            }
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
                dispatch({type: FETCH_MOVIE_SCREENING_STARTED});
                // const stringDate = `${dateToFilterBy.getDate()}-${dateToFilterBy.getMonth()+1}-${dateToFilterBy.getFullYear()}`;
                const moviesAux = await getMovieScreening();
                if (!canceled) {
                    dispatch({type: FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO, payload: {items: moviesAux}});
                }
            } catch (error) {
                dispatch({type: FETCH_MOVIE_INFO_FINISHED, payload: {error}});
            }
        }
    }
}
