import React, {useContext, useEffect, useState} from 'react';
import {CardTitle, Col, Form, Row} from "react-bootstrap";
import {RouteComponentProps} from "react-router";
import {useSearchParams} from 'react-router-dom';
import {MovieContext} from "./providers/MovieScreeningProvider.tsx";
import {SeatProps} from "./SeatProps.tsx";
import {getMovieHallSeats} from "./MovieHallApi.tsx";
import '../css/ReservationSelectSeats.css';
import {usePreferences} from "./usePreferences.ts";
import Button from "react-bootstrap/Button";
import {MovieScreeningProps} from "./MovieScreeningProps.tsx";
import {ReservationContext} from "./providers/ReservationProvider.tsx";
import Container from "react-bootstrap/Container";

interface ReservationSelectSeatsExt extends RouteComponentProps {
    uuid?: string;
}

const ConfirmSeatSelection: React.FC<ReservationSelectSeatsExt> = ({history, match}) => {
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
                    <Form className="d-flex" style={{color: '#767676'}}>
                        <Form.Label>{'Movie: '}</Form.Label>
                        <Form.Label type="text">{movie_screening.movie.title}</Form.Label>
                    </Form>
                    <Form className="d-flex" style={{color: '#767676'}}>
                        <Form.Label>Date: </Form.Label>
                        <Form.Label type="text">{movie_screening.date.toString()}</Form.Label>
                    </Form>
                    <Form className="d-flex" style={{color: '#767676'}}>
                        <Form.Label>Time: </Form.Label>
                        <Form.Label type="text">{movie_screening.times[index!].split(' ')[1]}</Form.Label>
                    </Form>
                    <Form className="d-flex" style={{color: '#767676'}}>
                        <Form.Label>Movie hall: </Form.Label>
                        <Form.Label type="text">{selectedSeats[0].movieHall.id}</Form.Label>
                    </Form>
                    <Form className="d-flex" style={{color: '#767676'}}>
                        <Form.Label>Seats: </Form.Label>
                        {
                            selectedSeats.map(({row_number, seat_number}) =>
                                <Form.Label type="text">{alphabet[row_number].toUpperCase() + '/' + seat_number}</Form.Label>
                            )
                        }
                    </Form>
                    <Form className="d-flex" style={{color: '#767676'}}>
                        <Button>Make reservation</Button>
                    </Form>
                </Container>
            )}
            {!selectedSeats && (
                <p>Select at least a seat!</p>
            )}
        </div>
    )
        ;
};

export default ConfirmSeatSelection;
