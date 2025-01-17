import React, {useCallback, useEffect, useReducer} from 'react';
import PropTypes from 'prop-types';
import {MovieScreeningProps} from './MovieScreeningProps';
import {getMovieInfo, getMovieScreening} from "./MovieScreeningApi.tsx";
import {usePreferences} from "./usePreferences.ts";

type SaveMovieFn = (movie_screening: MovieScreeningProps) => Promise<any>;
type PaginationFunction = (page_no: number) => void;
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
    currentPage: number,
    tab_no?: number,
    total_pages?: number
}

interface ActionProps {
    type: string,
    payload?: any
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
const FETCH_MOVIE_INFO_ERROR = 'FETCH_MOVIE_INFO_ERROR';
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
                return {
                    ...state,
                    currentPage: payload.page_no,
                    items: allItems.filter(movie_screening => movie_screening.date.toString() === payload.date.toLocaleDateString()).slice(payload.page_no * NUMBER_OF_MOVIES_PER_PAGE, payload.page_no * NUMBER_OF_MOVIES_PER_PAGE + NUMBER_OF_MOVIES_PER_PAGE),
                    fetching: false
                };
            }
            case
            PAGINATION_ERROR:
                return {...state, fetchingError: payload.error};
            case
            FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO: {
                return {
                    ...state,
                    allItems: payload.allItems.data.responseObject,
                    fetching: false
                };
            }
            case
            FETCH_MOVIE_INFO_STARTED:
                return {...state, tab_no: (payload.date.getDay() + 6) % 7, fetching: true, fetchingError: null};
            case
            FETCH_MOVIE_INFO_FINISHED: {
                const allItems = [...(state.allItems || [])];
                return {
                    ...state,
                    allItems: payload.allItems,
                    items: allItems.filter(movie_screening => movie_screening.date.toString() === payload.date.toLocaleDateString()).slice(payload.page_no * NUMBER_OF_MOVIES_PER_PAGE, payload.page_no * NUMBER_OF_MOVIES_PER_PAGE + NUMBER_OF_MOVIES_PER_PAGE),
                    currentPage: payload.page_no,
                    fetching: false
                };
            }
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
                // console.log([...(state.allItems || [])])
                return {...state, allItems, fetching: true, fetchingError: null};
            }
            case FETCH_MOVIE_INFO_ERROR:
                return {...state};
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
    const {items, allItems, fetching, fetchingError, saving, savingError, currentPage, tab_no} = state;
    const {get, set} = usePreferences();
    useEffect(getMoviesEffect, []);
    useEffect(getMovieInformation, [allItems, currentDate])
    const paginationFunction = useCallback<PaginationFunction>(paginationCallBack, [allItems, currentDate]);
    const setItems = useCallback<MoviesFn>(setItemsCallback, []);
    const value = {items, fetching, fetchingError, paginationFunction, setItems, currentPage, tab_no};
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

    function paginationCallBack(page_no: number) {
        let canceled = false;
        fetchPaginationData();
        return () => {
            canceled = true;
        }

        async function fetchPaginationData() {
            try {
                if (allItems && tab_no) {
                    if (!canceled) {
                        const replace_pages_string = await get('current_page');
                        const replace_pages = (replace_pages_string ? JSON.parse(replace_pages_string) : [])
                        if (replace_pages && replace_pages.length !== 0) {
                            replace_pages[tab_no] = page_no;
                            await set('current_page', JSON.stringify(replace_pages))
                            dispatch({type: PAGINATION_FINISHED, payload: {date: currentDate, page_no: page_no}});
                        }
                    }
                }
            } catch (error) {
                dispatch({type: PAGINATION_ERROR, payload: {error}});
            }
        }
    }

    function getMovieInformation() {
        let canceled = false;
        fetchMovieInformation();
        return () => {
            canceled = true;
        }

        async function fetchMovieInformation() {
            try {
                dispatch({type: FETCH_MOVIE_INFO_STARTED, payload: {date: currentDate}});
                let page_no = currentPage;
                const tab_no = (currentDate.getDay() + 6) % 7;
                if (tab_no) {
                    const saved_page_json = await get('current_page')
                    const saved_page = (saved_page_json ? JSON.parse(saved_page_json) : [])
                    if (saved_page && saved_page.length !== 0) {
                        page_no = saved_page[tab_no]
                    }
                    if (saved_page && saved_page.length === 0) {
                        await set('current_page', JSON.stringify([0, 0, 0, 0, 0, 0, 0]))
                    }
                    if (allItems) {
                        for (let i = 0; i < allItems.length; i++) {
                            if (!allItems[i].movie && allItems[i].date.toString() === currentDate.toLocaleDateString()) {
                                const uuid: number = allItems[i].movie_uuid!;
                                const movieInfo = await getMovieInfo(uuid);
                                dispatch({
                                    type: FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING,
                                    payload: {item: movieInfo}
                                });
                            }
                        }
                    }
                    if (!canceled) {
                        dispatch({
                            type: FETCH_MOVIE_INFO_FINISHED,
                            payload: {allItems: allItems, date: currentDate, page_no: page_no}
                        });
                    }
                }
            } catch (error) {
                dispatch({type: FETCH_MOVIE_INFO_ERROR, payload: {error}});
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
                const moviesAux = await getMovieScreening();
                if (!canceled) {
                    dispatch({
                        type: FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO,
                        payload: {allItems: moviesAux}
                    });
                }
            } catch (error) {
                dispatch({type: FETCH_MOVIE_INFO_ERROR, payload: {error}});
            }
        }
    }
}
