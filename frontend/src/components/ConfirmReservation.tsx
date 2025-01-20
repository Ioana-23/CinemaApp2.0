import React, {useContext, useEffect, useState} from 'react';
import {Col, Row} from "react-bootstrap";
import {RouteComponentProps} from "react-router";
import {useSearchParams} from 'react-router-dom';
import {MovieContext} from "./providers/MovieScreeningProvider.tsx";
import {SeatProps} from "./SeatProps.tsx";
import {getMovieHallSeats} from "./MovieHallApi.tsx";
import '../css/ReservationSelectSeats.css';
import {usePreferences} from "./usePreferences.ts";
import Button from "react-bootstrap/Button";

interface ReservationSelectSeatsExt extends RouteComponentProps {
    uuid?: string;
}

const ConfirmReservation: React.FC<ReservationSelectSeatsExt> = ({history, match}) => {
    const {
        allItems
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
    const [selectedSeats, setSelectedSeats] = useState<SeatProps[]>([]);
    const {get, set} = usePreferences();
    useEffect(configureHall, [seats]);
    useEffect(() => {
        const func = (async () => {
            const selected_seats_json = await get('selected_seats')
            const selected_seats_local: SeatProps[] = (selected_seats_json ? JSON.parse(selected_seats_json) : [])
            if (selected_seats_local.length != 0) {
                setSelectedSeats(selected_seats_local)
            }
        });
        func();
    }, []);
    useEffect(getSeats, [movie_hall_id, allItems]);

    function configureHall() {
        try {
            if (seats && seats.length > 0) {
                if (selectedSeats.length > 0) {
                    document.getElementById("continue_btn")!.disabled = false;
                    document.getElementById("continue_btn")!.className = "continue_btn";
                } else {
                    document.getElementById("continue_btn")!.disabled = true;
                    document.getElementById("continue_btn")!.className = "continue_btn_disabled";
                }
                const config = [];
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
                                console.log('heree')
                                row.push(<Col style={{paddingLeft: '1rem', alignItems: 'center'}}
                                              className="d-flex flex-column seat-box" key={seats[count].uuid}>
                                    <a
                                        id={count.toString()}
                                        className="seat-link-selected" data-key={seats[count].id}
                                        onClick={(event) => {
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
        } catch (error) {
            console.log({error})
        }
    }

    const handleContinueWithPayment = () => {
        const setInLocalStorage = (async () => await set('selected_seats', JSON.stringify([])));
        setInLocalStorage();
        setSelectedSeats([]);
    }

    const handleClickSeats = (value: number, id: string) => {
        const selectedSeat = seats[parseInt(id)];
        const indexInSeat = selectedSeats.findIndex(seat => selectedSeat.row_number == seat.row_number && selectedSeat.seat_number == seat.seat_number);
        let allSelectedSeats = selectedSeats
        if (indexInSeat === -1) {
            document.getElementById(id)!.className = "seat-link-selected"
            allSelectedSeats.push(selectedSeat)
        } else {
            document.getElementById(id)!.className = "seat-link"
            allSelectedSeats.splice(indexInSeat, 1);
        }
        if (allSelectedSeats.length > 0) {
            document.getElementById("continue_btn")!.disabled = false;
            document.getElementById("continue_btn")!.className = "continue_btn";
        } else {
            document.getElementById("continue_btn")!.disabled = true;
            document.getElementById("continue_btn")!.className = "continue_btn_disabled";
        }
        setSelectedSeats(allSelectedSeats)
        console.log({allSelectedSeats})
        const setInLocalStorage = (async () => await set('selected_seats', JSON.stringify(allSelectedSeats)));
        setInLocalStorage();
    };

    return (
        <div>
            <p style={{color: '#767676'}}>Ecran</p>
            {hall_configuration && selectedSeats && (
                <>
                    <div className="d-grid hall-configuration">
                        {hall_configuration}
                    </div>
                </>
            )}
            <Button id="continue_btn" className="continue_btn"
                    onClick={() => handleContinueWithPayment()}>Continue</Button>
        </div>
    )
        ;
};

export default ConfirmReservation;
