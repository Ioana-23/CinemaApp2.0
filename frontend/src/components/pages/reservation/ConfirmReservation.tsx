import React, {useContext, useEffect} from 'react';
import {CardTitle, Form, FormGroup} from "react-bootstrap";
import {RouteComponentProps} from "react-router";
import {MovieContext} from "../../providers/MovieScreeningProvider.tsx";
import {usePreferences} from "../../usePreferences.ts";
import Button from "react-bootstrap/Button";
import {ReservationContext} from "../../providers/ReservationProvider.tsx";
import Container from "react-bootstrap/Container";
import '../../../css/ConfirmReservation.css';

interface ReservationSelectSeatsExt extends RouteComponentProps {
    uuid?: string;
}

const ConfirmReservation: React.FC<ReservationSelectSeatsExt> = ({history, match}) => {
    const {allItems} = useContext(MovieContext);
    const {selectedSeats, movie_screening, getSeats, index, reservation, saveReservationToDB} = useContext(ReservationContext);
    const {get, set} = usePreferences();
    const alphabet: string[] = "abcdefghijklmnopqrstuvw".toUpperCase().split('')
    return (
        <div>
            {reservation && reservation.uuid == null && selectedSeats && movie_screening?.movie && index! >= 0 && saveReservationToDB && (
                <Container>
                    <CardTitle style={{color: '#767676'}}>Reservation Details</CardTitle>
                    <FormGroup className="confirm_reservation_container">
                        <Form>
                            <Form.Label className="form_label">Movie:</Form.Label>
                            <Form.Label type="text">&nbsp;{movie_screening.movie.title}</Form.Label>
                        </Form>
                        <Form>
                            <Form.Label className="form_label">Date:</Form.Label>
                            <Form.Label type="text">&nbsp;{movie_screening.date.toString()}</Form.Label>
                        </Form>
                        <Form>
                            <Form.Label className="form_label">Time:</Form.Label>
                            <Form.Label type="text">&nbsp;{movie_screening.times[index!].split(' ')[1]}</Form.Label>
                        </Form>
                        <Form>
                            <Form.Label className="form_label">Movie hall:</Form.Label>
                            <Form.Label type="text">&nbsp;{movie_screening.movieHall_uuid[index!]}</Form.Label>
                        </Form>
                        <Form>
                            <Form.Label className="form_label">Seats:</Form.Label>
                            {
                                selectedSeats.map(({row_number, seat_number}) =>
                                    <Form.Label
                                        type="text">&nbsp;{alphabet[row_number-1].toUpperCase() + '/' + `${seat_number-1}`}</Form.Label>
                                )
                            }
                        </Form>
                        <button className="confirmation_button" onClick={() => reservation ? saveReservationToDB(reservation) : {}}>Make reservation</button>
                    </FormGroup>

                </Container>
            )}
            {reservation && reservation.uuid == null && !selectedSeats && (
                <p>Select at least a seat!</p>
            )}
            {reservation && reservation.uuid != null && (
                <p>Congrats!</p>
            )}
        </div>
    )
        ;
};

export default ConfirmReservation;
