import React, {useCallback, useEffect, useReducer} from 'react';
import PropTypes from 'prop-types';
import {MovieScreeningProps} from './MovieScreeningProps';
import {getMovieInfo, getMovieScreening} from "./MovieScreeningApi.tsx";
import {all} from "axios";

type SaveMovieFn = (movie_screening: MovieScreeningProps) => Promise<any>;
type PaginationFunction = () => void;
type MoviesFn = (movie_screening: MovieScreeningProps[]) => void;

export const NUMBER_OF_MOVIES_PER_PAGE = 1;

export interface MovieScreeningState {
    items?: MovieScreeningProps[],
    allItems?: MovieScreeningProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveItem?: SaveMovieFn,
    paginationFunction?: PaginationFunction,
    setItems?: MoviesFn,
    currentPage: number
}

interface ActionProps {
    type: string,
    payload?: any,
}

export const initialStateItems: MovieScreeningState = {
    fetching: false,
    saving: false,
    currentPage: 0
};

export const MovieContext = React.createContext<MovieScreeningState>(initialStateItems);

const FETCH_MOVIE_SCREENING_STARTED = 'FETCH_MOVIE_SCREENING_STARTED';
const FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO = 'FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO';
const FETCH_MOVIE_INFO_STARTED = 'FETCH_MOVIE_INFO_STARTED';
const FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING = 'FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING';
const FETCH_MOVIE_INFO_FINISHED = 'FETCH_MOVIE_INFO_FINISHED';
const FETCH_MOVIE_SCREENING_FINISHED = 'FETCH_MOVIE_SCREENING_FINISHED';
const PAGINATION_FINISHED = 'PAGINATION_FINISHED'
const PAGINATION_ERROR = 'PAGINATION_ERROR'
const reducer: (state: MovieScreeningState, action: ActionProps) => MovieScreeningState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_MOVIE_SCREENING_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case PAGINATION_FINISHED: {
                const allItems = [...(state.allItems || [])];
                console.log(state.currentPage)
                return {
                    ...state,
                    currentPage: state.currentPage + 1,
                    items: allItems.filter(movie_screening => movie_screening.date.toString() === payload.date.toLocaleDateString()).slice((state.currentPage + 1) * NUMBER_OF_MOVIES_PER_PAGE, (state.currentPage + 2) * NUMBER_OF_MOVIES_PER_PAGE + NUMBER_OF_MOVIES_PER_PAGE),
                    fetching: false
                };}
            case
            PAGINATION_ERROR:
                return {...state, fetchingError: payload.error};
            case
            FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO: {
                return {
                    ...state,
                    allItems: payload.allItems.data.responseObject,
                    fetching: false
                };}
            case
            FETCH_MOVIE_INFO_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case
            FETCH_MOVIE_INFO_FINISHED: {
                const allItems = [...(state.allItems || [])];
                let page_no = state.currentPage;
                const items = [...(state.items || [])];
                if(items.length !== 0 && items[0].date !== payload.date)
                {
                    page_no = 0;
                }
                return {
                    ...state,
                    allItems: payload.allItems,
                    items: allItems.filter(movie_screening => movie_screening.date.toString() === payload.date.toLocaleDateString()).slice(page_no * NUMBER_OF_MOVIES_PER_PAGE, page_no * NUMBER_OF_MOVIES_PER_PAGE + NUMBER_OF_MOVIES_PER_PAGE),
                    currentPage: page_no,
                    fetching: false
                };}
            case
            FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING: {
                const allItems = [...(state.allItems || [])];
                const item = payload.item.data.responseObject
                let index = allItems.findIndex(it => it.movie_uuid === item.uuid);
                while (index != -1 && index < allItems.length) {
                    if (allItems[index].movie_uuid === item.uuid) {
                        allItems[index].movie = item;
                    }
                    index++;
                }
                console.log([...(state.allItems || [])])
                return {...state, allItems, fetching: true, fetchingError: null};
            }
            default:
                return state;
        }
    };

interface MovieScreeningProviderProps {
    children: PropTypes.ReactNodeLike,
    currentDate: Date
}

export const MovieScreeningProvider: React.FC<MovieScreeningProviderProps> = ({children, currentDate}) => {
    const [state, dispatch] = useReducer(reducer, initialStateItems);
    const {items, allItems, fetching, fetchingError, saving, savingError, currentPage} = state;
    useEffect(getMoviesEffect, []);
    useEffect(getMovieInformation, [allItems, currentDate])
    const paginationFunction = useCallback<PaginationFunction>(paginationCallBack, [allItems, currentPage]);
    const setItems = useCallback<MoviesFn>(setItemsCallback, []);
    const value = {items, fetching, fetchingError, paginationFunction, setItems, currentPage};
    return (
        <MovieContext.Provider value={value}>
            {children}
        </MovieContext.Provider>
    );

    async function setItemsCallback(movie_screening: MovieScreeningProps[]) {
        await setItems();

        function setItems() {
            dispatch({type: FETCH_MOVIE_SCREENING_FINISHED, payload: {items: movie_screening, allItems: allItems}});
        }
    }

    function paginationCallBack() {
        let canceled1 = false;
        fetchPaginationData();
        return () => {
            canceled1 = true;
        }

        async function fetchPaginationData() {
            try {
                const items_on_page: MovieScreeningProps[] = [];
                console.log('heree')
                if (allItems) {
                    for (let i = currentPage * NUMBER_OF_MOVIES_PER_PAGE; i < Math.min(currentPage * NUMBER_OF_MOVIES_PER_PAGE + NUMBER_OF_MOVIES_PER_PAGE, allItems.length); i++) {
                        items_on_page.push(allItems[i])
                    }
                    if (!canceled1) {
                        console.log({items_on_page})
                        dispatch({type: PAGINATION_FINISHED, payload: {items: items_on_page, date: currentDate}});
                    }
                }
            } catch (error) {
                dispatch({type: PAGINATION_ERROR, payload: {error}});
            }
        }
    }

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
                if (allItems) {
                    for (let i = 0; i < allItems.length; i++) {
                        if (!allItems[i].movie && allItems[i].date.toString() === currentDate.toLocaleDateString()) {
                            const uuid: number = allItems[i].movie_uuid;
                            const movieInfo: Response = await getMovieInfo(uuid);
                            dispatch({type: FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING, payload: {item: movieInfo}});
                        }
                    }
                }
                if (!canceled1) {
                    dispatch({type: FETCH_MOVIE_INFO_FINISHED, payload: {allItems: allItems, date: currentDate}});
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
                    dispatch({
                        type: FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO,
                        payload: {allItems: moviesAux}
                    });
                }
            } catch (error) {
                dispatch({type: FETCH_MOVIE_INFO_FINISHED, payload: {error}});
            }
        }
    }
}
