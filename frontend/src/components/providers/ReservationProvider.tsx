import React, {useCallback, useEffect, useReducer} from 'react';
import PropTypes from 'prop-types';
import {MovieScreeningProps} from '../MovieScreeningProps.tsx';
import {usePreferences} from "../usePreferences.ts";
import {SeatProps} from "../SeatProps.tsx";
import {ActionProps} from './MovieScreeningProvider.tsx';
import {getMovieHallSeats} from "../MovieHallApi.tsx";
import {Col, Row} from "react-bootstrap";

type SaveMovieFn = (movie_screening: MovieScreeningProps) => Promise<any>;
type SelectSeatFn = (seat_uuid: number) => void;
type GetSeatsFn = (movie_hall_uuid: number) => void;

export interface ReservationState {
    seats?: SeatProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveItem?: SaveMovieFn,
    selectedSeats: SeatProps[],
    movie_screening_id?: number,
    getSeats?: GetSeatsFn,
    selectSeats?: SelectSeatFn,
    movie_hall_configuration: Element[]
}

export const initialStateReservations: ReservationState = {
    fetching: false,
    saving: false,
    selectedSeats: [],
    movie_hall_configuration: []
};

export const ReservationContext = React.createContext<ReservationState>(initialStateReservations);

const FETCH_SEATS_STARTED = 'FETCH_SEATS_STARTED';
const FETCH_SEATS_FINISHED = 'FETCH_SEATS_FINISHED';
const ADD_OR_REMOVE_SEAT_TO_SELECTION = 'ADD_OR_REMOVE_SEAT_TO_SELECTION';
const CONFIGURE_MOVIE_HALL = 'CONFIGURE_MOVIE_HALL';
const PAGINATION_FINISHED = 'PAGINATION_FINISHED'
const PAGINATION_ERROR = 'PAGINATION_ERROR'
const reducer: (state: ReservationState, action: ActionProps) => ReservationState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_SEATS_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_SEATS_FINISHED: {
                return {
                    ...state,
                    fetching: false,
                    seats: payload.seats.data.responseObject,
                    movie_hall_configuration: payload.movie_hall_configuration,
                    selectedSeats: payload.selectedSeats,
                    movie_screening_id: payload.movie_screening_id
                };
            }
            case ADD_OR_REMOVE_SEAT_TO_SELECTION: {
                const selectedSeats = [...(state.selectedSeats || [])];
                const selectedSeat = payload.selectedSeat;
                const indexInSeat = selectedSeats.findIndex(seat => selectedSeat.row_number == seat.row_number && selectedSeat.seat_number == seat.seat_number);
                if (indexInSeat === -1) {
                    selectedSeats.push(selectedSeat)
                } else {
                    selectedSeats.splice(indexInSeat, 1);
                }
                const setInLocalStorage = (async () => await payload.set('selected_seats', JSON.stringify(selectedSeats)));
                setInLocalStorage();
                return {...state, selectedSeats: selectedSeats}
            }
            case CONFIGURE_MOVIE_HALL:
                return {...state, movie_hall_configuration: payload.movie_hall_configuration};
            //     case PAGINATION_FINISHED: {
            //         const allItems = [...(state.allItems || [])];
            //         const newPagination = [];
            //         // const first_page_seen = Math.max(payload.page_no - 1, 0);
            //         const first_page_seen = 0;
            //         const last_page_seen = state.total_pages!;
            //         // const last_page_seen = Math.min(payload.page_no + 2, state.total_pages!);
            //         // if(first_page_seen !== 0)
            //         // {
            //         //     newPagination.push(<Pagination.Prev></Pagination.Prev>);
            //         // }
            //         for (let i = first_page_seen; i < last_page_seen; i++) {
            //             newPagination.push(<Pagination.Item key={i} active={payload.page_no === i}
            //                                                 onClick={() => payload.paginationFunction?.(i)}>{i + 1}</Pagination.Item>)
            //         }
            //         // if(last_page_seen !== state.total_pages! && last_page_seen < state.total_pages! + 1)
            //         // {
            //         //     newPagination.push(<Pagination.Ellipsis></Pagination.Ellipsis>)
            //         //     newPagination.push(<Pagination.Item key={state.total_pages!-1} active={payload.page_no === state.total_pages!-1}
            //         //                                         onClick={() => payload.paginationFunction?.(state.total_pages!-1)}>{state.total_pages!}</Pagination.Item>)
            //         //     newPagination.push(<Pagination.Next></Pagination.Next>)
            //         // }
            //         return {
            //             ...state,
            //             currentPage: payload.page_no,
            //             items: allItems.filter(movie_screening => movie_screening.date.toString() === payload.date.toLocaleDateString()).slice(payload.page_no * payload.number_movies_per_page, payload.page_no * payload.number_movies_per_page + payload.number_movies_per_page),
            //             pagination_ribbon: newPagination,
            //             fetching: false
            //         };
            //     }
            //     case
            //     PAGINATION_ERROR:
            //         return {...state, fetchingError: payload.error};
            //     case
            //     FETCH_MOVIE_SCREENING_SUCCEEDED_FETCHING_MOVIE_INFO: {
            //         return {
            //             ...state,
            //             allItems: payload.allItems.data.responseObject,
            //             fetching: false
            //         };
            //     }
            //     case
            //     FETCH_MOVIE_INFO_STARTED:
            //         return {...state, tab_no: (payload.date.getDay() + 6) % 7, fetching: true, fetchingError: null};
            //     case
            //     FETCH_MOVIE_INFO_FINISHED: {
            //         const allItems = [...(state.allItems || [])];
            //         const newItems = allItems.filter(movie_screening => movie_screening.date.toString() === payload.date.toLocaleDateString());
            //         console.log({newItems})
            //         const new_total_pages = Math.round(newItems.length / payload.number_movies_per_page);
            //         const newPagination = [];
            //         // const first_page_seen = Math.max(payload.page_no - 1, 0);
            //         const first_page_seen = 0;
            //         const last_page_seen = new_total_pages;
            //         for (let i = first_page_seen; i < last_page_seen; i++) {
            //             newPagination.push(<Pagination.Item key={i} active={payload.page_no === i}
            //                                                 onClick={() => payload.paginationFunction?.(i)}>{i + 1}</Pagination.Item>)
            //         }
            //         const new_pagination_items = newItems.slice(payload.page_no * payload.number_movies_per_page, payload.page_no * payload.number_movies_per_page + payload.number_movies_per_page);
            //         return {
            //             ...state,
            //             allItems: payload.allItems,
            //             items: new_pagination_items,
            //             currentPage: payload.page_no,
            //             total_pages: new_total_pages,
            //             pagination_ribbon: newPagination,
            //             fetching: false
            //         };
            //     }
            //     case
            //     FETCH_MOVIE_INFORMATION_FOR_MOVIE_SCREENING: {
            //         const allItems = [...(state.allItems || [])];
            //         const item = payload.item.data.responseObject
            //         let index = allItems.findIndex(it => it.movie_uuid === item.uuid);
            //         while (index != -1 && index < allItems.length) {
            //             if (allItems[index].movie_uuid === item.uuid) {
            //                 allItems[index].movie = item;
            //             }
            //             index++;
            //         }
            //         // console.log([...(state.allItems || [])])
            //         return {...state, allItems, fetching: true, fetchingError: null};
            //     }
            //     case FETCH_MOVIE_INFO_ERROR:
            //         return {...state};
            default:
                return state;
        }
    };

interface ReservationProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const ReservationProvider: React.FC<ReservationProviderProps> = ({
                                                                            children
                                                                        }) => {
    const [state, dispatch] = useReducer(reducer, initialStateReservations);
    const {seats, fetching, fetchingError, saving, savingError, saveItem, selectedSeats, movie_screening_id, movie_hall_configuration} = state;
    const {get, set} = usePreferences();
    const getSeats = useCallback<GetSeatsFn>(getSeatsFromDatabase, [movie_screening_id]);
    const selectSeats = useCallback<SelectSeatFn>(selectSeatByUuid, [seats]);

    function configure_movie_hall() {
        if(seats && selectSeats)
        {
            const hall_configuration = [];
            const row_no = seats[seats.length - 1].row_number;
            const col_no = seats[seats.length - 1].seat_number;
            const alphabet: string[] = "abcdefghijklmnopqrstuvw".toUpperCase().split('')
            let count = 0;
            for (let i = 0; i < row_no; i++) {
                const row = [];
                for (let j = 0; j < col_no; j++) {
                    const height = 100 / row_no;
                    if (seats[count].available) {
                        if (selectedSeats.length != 0 && selectedSeats.findIndex(seat => seat.row_number == seats[count].row_number && seat.seat_number == seats[count].seat_number) !== -1) {
                            row.push(<Col style={{paddingLeft: '1rem', alignItems: 'center'}}
                                          className="d-flex flex-column seat-box" key={seats[count].uuid}>
                                <a
                                    id={count.toString()}
                                    className="seat-link-selected" data-key={seats[count].id}
                                    onClick={(event) => {
                                        selectSeats(event.target.id)
                                    }}
                                    style={{
                                        height: `${height * 4}%`,
                                        width: `${height * 4}%`
                                    }}>{alphabet[i]}/{j}</a>
                            </Col>)
                        } else {
                            row.push(<Col style={{paddingLeft: '1rem', alignItems: 'center'}}
                                          className="d-flex flex-column seat-box" key={seats[count].uuid}>
                                <a
                                    id={count.toString()}
                                    className="seat-link" data-key={count} onClick={(event) => {
                                    selectSeats(event.target.id)
                                }}
                                    style={{
                                        height: `${height * 4}%`,
                                        width: `${height * 4}%`
                                    }}>{alphabet[i]}/{j}</a>
                            </Col>)
                        }
                    } else {
                        row.push(<Col style={{paddingLeft: '1rem', alignItems: 'center'}}
                                      className="d-flex flex-column seat-box" key={seats[count].uuid}> <a
                            className="seat-link-disabled disabled"
                            data-key={count}
                            style={{
                                height: `${height * 4}%`,
                                width: `${height * 4}%`
                            }}>{alphabet[i]}/{j}</a>
                        </Col>)
                    }
                    count++;
                }
                hall_configuration.push(<Row className="d-flex flex-row">{row}</Row>)
            }
            dispatch({type: CONFIGURE_MOVIE_HALL, payload: {movie_hall_configuration: hall_configuration}})
        }

    }

    useEffect(() => {
        configure_movie_hall();
    }, [seats, selectedSeats]);
    function getSeatsFromDatabase(movie_hall_id: number) {
        let canceled = false;
        getSeatsAsync();
        return () => {
            canceled = true;
        }

        async function getSeatsAsync() {
            try {
                dispatch({type: FETCH_SEATS_STARTED})
                if (movie_hall_id >= 0) {
                    const seats = await getMovieHallSeats(movie_hall_id)
                    const selected_seats_json = await get('selected_seats');
                    const selected_seats_local: SeatProps[] = (selected_seats_json ? JSON.parse(selected_seats_json) : [])
                    if (!canceled) {
                        dispatch({
                            type: FETCH_SEATS_FINISHED,
                            payload: {seats: seats, movie_screening_id: movie_screening_id, selectSeats: selectSeats, selectedSeats: selected_seats_local}
                        })
                    }
                }
            } catch (error) {
                console.log({error})
            }
        }
    }

    function selectSeatByUuid(seat_uuid: number) {
        let canceled = false;
        selectSeatAsync();
        return () => {
            canceled = true;
        }

        async function selectSeatAsync() {
            if (seats) {
                const selectedSeat = seats[seat_uuid];
                if (!canceled) {
                    dispatch({type: ADD_OR_REMOVE_SEAT_TO_SELECTION, payload: {selectedSeat: selectedSeat, set: set}})
                }
            }
        }
    }
    const value = {
        seats,
        fetching,
        fetchingError,
        saving,
        savingError,
        saveItem,
        selectedSeats,
        movie_screening_id,
        getSeats, selectSeats, movie_hall_configuration
    };
    return (
        <ReservationContext.Provider value={value}>
            {children}
        </ReservationContext.Provider>
    );
}
