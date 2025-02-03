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
    const {selectedSeats, movie_screening, getSeats, index} = useContext(ReservationContext);
    const {get, set} = usePreferences();
    useEffect(() => {
        if (selectedSeats && movie_screening && index >= 0) {
        }
    }, [selectedSeats, movie_screening, index]);
    const alphabet: string[] = "abcdefghijklmnopqrstuvw".toUpperCase().split('')
    return (
        <div>
            {selectedSeats && movie_screening?.movie && index! >= 0 && (
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
                            <Form.Label type="text">&nbsp;{selectedSeats[0].movieHall.id}</Form.Label>
                        </Form>
                        <Form>
                            <Form.Label className="form_label">Seats:</Form.Label>
                            {
                                selectedSeats.map(({row_number, seat_number}) =>
                                    <Form.Label
                                        type="text">&nbsp;{alphabet[row_number-1].toUpperCase() + '/' + seat_number}</Form.Label>
                                )
                            }
                        </Form>
                        <Button className="confirmation_button" onClick={() => console.log('')}>Make reservation</Button>
                    </FormGroup>

                </Container>
            )}
            {!selectedSeats && (
                <p>Select at least a seat!</p>
            )}
        </div>
    )
        ;
};

export default ConfirmReservation;
