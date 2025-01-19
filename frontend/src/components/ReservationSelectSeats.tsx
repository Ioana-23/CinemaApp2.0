import React, {useCallback, useContext, useEffect, useState} from 'react';
import {Col, Row} from "react-bootstrap";
import '../css/MovieScreeningCard.css';
import {RouteComponentProps} from "react-router";
import {useSearchParams} from 'react-router-dom';
import {MovieContext} from "./MovieScreeningProvider.tsx";
import {SeatProps} from "./SeatProps.tsx";
import {getMovieHallSeats} from "./MovieHallApi.tsx";
import '../css/ReservationSelectSeats.css';
import {usePreferences} from "./usePreferences.ts";

interface ReservationSelectSeatsExt extends RouteComponentProps {
    uuid?: string;
}

const ReservationSelectSeats: React.FC<ReservationSelectSeatsExt> = ({history, match}) => {
    const {
        allItems,
        items,
        fetching,
        fetchingError,
        paginationFunction,
        currentPage,
        tab_no,
        total_pages,
        pagination_ribbon
    } = useContext(MovieContext);
    const [searchParams] = useSearchParams();
    const [movie_screening_id, setMovie_screening_id] = useState<number>(parseInt(searchParams.get('uuid')!.toString()) || -1)
    const [movie_screening_list, setMovie_screening_list] = useState<any>(null)
    const [movie_hall_id, setMovie_hall_id] = useState<number>(-1);
    useEffect(() => {
        if (allItems) {
            setMovie_screening_list(allItems!.find(movie_screening => movie_screening.uuid.includes(movie_screening_id)))
        }
        if (movie_screening_list) {
            setMovie_hall_id(movie_screening_list.movieHall_uuid[movie_screening_list.uuid.indexOf(movie_screening_id)])
        }
    }, [allItems, movie_screening_list]);
    const [seats, setSeats] = useState<SeatProps[]>([]);
    const [hall_configuration, setHall_configuration] = useState();
    const {get, set} = usePreferences();
    const handleClickSeats = (index: number, id: string) => {
        let selected_seats_local = [];
        let canceled = false;
        getSeatsFromLocalStorage();
        return () => {
            canceled = true;
        }

        async function getSeatsFromLocalStorage() {
            const selected_seats_json = await get('selected_seats');
            selected_seats_local = (selected_seats_json ? JSON.parse(selected_seats_json) : [])
            let selected_seats_aux = [];
            if (selected_seats_local.length != 0) {
                selected_seats_aux = selected_seats_local.map(function (seat) {
                    return {
                        id: parseInt(seat.id),
                        available: seat.available.toString() === "true",
                        row_number: parseInt(seat.row_number),
                        seat_number: parseInt(seat.seat_number),
                        movieHall: {
                            id:parseInt(seat.movieHall.id),
                            uuid: parseInt(seat.movieHall.uuid)
                        },
                        uuid: parseInt(seat.uuid)
                    };
                })
            }
            console.log({selected_seats_aux})
            const selectedSeat = seats[index];
            console.log({selectedSeat})
            const indexInSeat = selected_seats_aux.filter(seat => selectedSeat.row_number == seat.row_number && selectedSeat.seat_number == seat.seat_number);
            const allSelectedSeats = selected_seats_aux
            // console.log({selectedSeat})
            // console.log({allSelectedSeats})
            // console.log({indexInSeat})
            if (indexInSeat.length === 0) {
                document.getElementById(id)!.className = "seat-link-selected"
                allSelectedSeats.push(selectedSeat)
            } else {
                document.getElementById(id)!.className = "seat-link"
                allSelectedSeats.splice(indexInSeat, 1);
            }
            if (!canceled) {
                const setInLocalStorage = (async () => await set('selected_seats', JSON.stringify(allSelectedSeats)));
                setInLocalStorage();
            }
        }
    };
    useEffect(getSeats, [movie_hall_id, allItems]);
    useEffect(configureHall, [seats]);

    function getSeats() {
        let canceled = false;
        getSeatsAsync();
        return () => {
            canceled = true;
        }

        async function getSeatsAsync() {
            try {
                if (movie_hall_id >= 0) {
                    const seats = await getMovieHallSeats(movie_hall_id)
                    if (!canceled) {
                        setSeats(seats.data.responseObject)
                    }
                }
            } catch (error) {
                console.log({error})
            }
        }
    }

    function configureHall() {
        let canceled = false;
        configureHallBySeats();
        return () => {
            canceled = true;
        }

        async function configureHallBySeats() {
            try {
                console.log({seats})
                if (seats && seats.length > 0) {
                    const selected_seats_json = await get('selected_seats')
                    const selected_seats_local = (selected_seats_json ? JSON.parse(selected_seats_json) : [])
                    if (selected_seats_local.length != 0) {
                        const selected_seats_aux = selected_seats_local.map(function (seat) {
                            return {
                                row_number: parseInt(seat.row_number),
                                seat_number: parseInt(seat.seat_number),
                                uuid: parseInt(seat.uuid),
                                movie_hall_uuid: parseInt(seat.movie_hall_uuid),
                                available: seat.available == 'true'
                            };
                        })
                        // setSelectedSeats(selected_seats_aux)
                    }
                    if (!canceled) {
                        const config = [];
                        const row_no = seats[seats.length - 1].row_number;
                        const col_no = seats[seats.length - 1].seat_number;
                        const alphabet: string[] = "abcdefghijklmnopqrstuvw".toUpperCase().split('')
                        let count = 0;
                        for (let i = 0; i < row_no; i++) {
                            const row = [];
                            for (let j = 0; j < col_no; j++) {
                                // console.log(`seats[${count}].uuid=${seats[count].uuid}`)
                                const height = 100 / row_no;
                                if (seats[count].available) {
                                    if (selected_seats_local.length != 0 && selected_seats_local.filter(seat => seat.row_number == seats[count].row_number && seat.seat_number == seats[count].seat_number).length !== 0) {
                                        console.log('heree')
                                        row.push(<Col style={{paddingLeft: '1rem', alignItems: 'center'}}
                                                      className="d-flex flex-column seat-box" key={seats[count].uuid}>
                                            <a
                                                id={count.toString()}
                                                className="seat-link-selected" data-key={count} onClick={(event) => {
                                                const value = parseInt(event.target.attributes[2].nodeValue);
                                                const id = event.target.id
                                                handleClickSeats(value, id)
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
                                                const value = parseInt(event.target.attributes[2].nodeValue);
                                                const id = event.target.id
                                                handleClickSeats(value, id)
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
                            config.push(<Row className="d-flex flex-row">{row}</Row>)
                        }
                        setHall_configuration(config)
                    }
                }
            } catch (error) {
                console.log({error})
            }
        }
    }

    return (
        <div>
            <p style={{color: '#767676'}}>Ecran</p>
            {hall_configuration && (
                <div className="d-grid hall-configuration">
                    {hall_configuration}
                </div>
            )}
        </div>
    );
};

export default ReservationSelectSeats;
