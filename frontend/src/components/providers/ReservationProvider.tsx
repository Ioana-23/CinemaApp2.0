import React, {useCallback, useContext, useEffect, useReducer} from 'react';
import PropTypes from 'prop-types';
import {MovieScreeningProps} from '../MovieScreeningProps.tsx';
import {usePreferences} from "../usePreferences.ts";
import {SeatProps} from "../SeatProps.tsx";
import {ActionProps, MovieContext} from './MovieScreeningProvider.tsx';
import {getMovieHallSeats} from "../MovieHallApi.tsx";
import {Col, Row} from "react-bootstrap";
import {getMovieInfo} from "../MovieScreeningApi.tsx";

type ConfirmSeats = () => void;
type SelectSeatFn = (seat_uuid: number) => void;
type GetSeatsFn = (uuid: number) => void;

export interface ReservationState {
    seats?: SeatProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    selectedSeats: SeatProps[],
    movie_screening?: MovieScreeningProps,
    getSeats?: GetSeatsFn,
    selectSeats?: SelectSeatFn,
    movie_hall_configuration: Element[],
    index?: number,
    confirmation_configuration: Element[],
}

export const initialStateReservations: ReservationState = {
    fetching: false,
    saving: false,
    selectedSeats: [],
    movie_hall_configuration: [],
    confirmation_configuration: []
};

export const ReservationContext = React.createContext<ReservationState>(initialStateReservations);

const FETCH_SEATS_STARTED = 'FETCH_SEATS_STARTED';
const FETCH_SEATS_FINISHED = 'FETCH_SEATS_FINISHED';
const ADD_OR_REMOVE_SEAT_TO_SELECTION = 'ADD_OR_REMOVE_SEAT_TO_SELECTION';
const CONFIGURE_MOVIE_HALL = 'CONFIGURE_MOVIE_HALL';
const FETCH_SELECTED_SEATS_FROM_LOCAL_STORAGE = 'FETCH_SELECTED_SEATS_FROM_LOCAL_STORAGE';
const reducer: (state: ReservationState, action: ActionProps) => ReservationState =
    (state, {type, payload}) => {
        switch (type) {
            case FETCH_SEATS_STARTED:
                return {...state, fetching: true, fetchingError: null};
            case FETCH_SEATS_FINISHED: {
                let movie_screening = payload.movie_screening;
                movie_screening.movie = payload.movieInfo.data.responseObject;
                return {
                    ...state,
                    fetching: false,
                    seats: payload.seats.data.responseObject,
                    movie_hall_configuration: payload.movie_hall_configuration,
                    movie_screening: payload.movie_screening,
                    index: payload.index
                };
            }
            case FETCH_SELECTED_SEATS_FROM_LOCAL_STORAGE: {
                const confirmation_configuration = [];
                let movie_info: string = "";
                if(payload.movie_screening)
                {
                    movie_info = payload.movie_screening.title;
                }
                confirmation_configuration.push(<Row>Movie: {payload.movie_screening}</Row>)
                return {
                    ...state,
                    selectedSeats: payload.selectedSeats,
                    index: payload.index,
                    movie_screening: payload.movie_screening,
                    confirmation_configuration: confirmation_configuration
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
    const {allItems} = useContext(MovieContext);
    const [state, dispatch] = useReducer(reducer, initialStateReservations);
    const {
        seats,
        fetching,
        fetchingError,
        saving,
        savingError,
        selectedSeats,
        movie_screening,
        movie_hall_configuration,
        index,
        confirmation_configuration
    } = state;
    const {get, set} = usePreferences();
    useEffect(() => {
        const getSeatsFromLocal = (async () => {
            const selected_seats_json = await get('selected_seats');
            const selected_seats_local: SeatProps[] = (selected_seats_json ? JSON.parse(selected_seats_json) : [])
            const movie_screening_json = await get('movie_screening');
            const movie_screening_local: MovieScreeningProps = (movie_screening_json ? JSON.parse(movie_screening_json) : null)
            const index_json = await get('index');
            const index_local: number = (index_json ? JSON.parse(index_json) : -1)
            dispatch({
                type: FETCH_SELECTED_SEATS_FROM_LOCAL_STORAGE,
                payload: {
                    selectedSeats: selected_seats_local,
                    set: set,
                    movie_screening: movie_screening_local,
                    index: index_local
                }
            })
        })
        getSeatsFromLocal();
    }, [seats]);
    const getSeats = useCallback<GetSeatsFn>(getSeatsFromDatabase, [allItems]);
    const selectSeats = useCallback<SelectSeatFn>(selectSeatByUuid, [seats, allItems]);


    function configure_movie_hall() {
        if (seats && selectSeats) {
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
    }, [seats, selectedSeats, allItems]);

    function getSeatsFromDatabase(uuid: number) {
        let canceled = false;
        getSeatsAsync();
        return () => {
            canceled = true;
        }

        async function getSeatsAsync() {
            try {
                dispatch({type: FETCH_SEATS_STARTED})
                if (allItems) {
                    const movie_screening = allItems!.find(movie_screening => movie_screening.uuid.includes(uuid))
                    if (movie_screening) {
                        const movieInfo = await getMovieInfo(movie_screening.movie_uuid);
                        const index = movie_screening.uuid.indexOf(uuid)
                        await set('movie_screening', JSON.stringify(movie_screening))
                        await set('index', JSON.stringify(index))
                        const movie_hall_id = movie_screening.movieHall_uuid[index]
                        if (movie_hall_id >= 0) {
                            const seats = await getMovieHallSeats(movie_hall_id)
                            if (!canceled) {
                                dispatch({
                                    type: FETCH_SEATS_FINISHED,
                                    payload: {
                                        seats: seats,
                                        movie_screening: movie_screening,
                                        selectSeats: selectSeats,
                                        index: index,
                                        movieInfo: movieInfo
                                    }
                                })
                            }
                        }
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
        selectedSeats,
        movie_screening,
        getSeats, selectSeats, movie_hall_configuration, index,confirmation_configuration
    };
    return (
        <ReservationContext.Provider value={value}>
            {children}
        </ReservationContext.Provider>
    );
}
