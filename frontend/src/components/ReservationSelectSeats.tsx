import React, {useContext, useEffect, useState} from 'react';
import {Col, Row} from "react-bootstrap";
import {RouteComponentProps} from "react-router";
import {useNavigate, useSearchParams} from 'react-router-dom';
import {MovieContext} from "./providers/MovieScreeningProvider.tsx";
import '../css/ReservationSelectSeats.css';
import Button from "react-bootstrap/Button";
import {ReservationContext} from "./providers/ReservationProvider.tsx";
import {MovieScreeningProps} from "./MovieScreeningProps.tsx";

interface ReservationSelectSeatsExt extends RouteComponentProps {
    uuid?: string;
}

const ReservationSelectSeats: React.FC<ReservationSelectSeatsExt> = ({history, match}) => {
    const {selectedSeats, getSeats, movie_hall_configuration} = useContext(ReservationContext);
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [movie_screening_id] = useState<number>(parseInt(searchParams.get('uuid')!.toString()) || -1)
    useEffect(() => {
        if(getSeats && movie_screening_id)
        {
            getSeats(movie_screening_id)
        }
    }, [getSeats, movie_screening_id]);
    function confirmSeats() {
        if(movie_screening_id)
        {
            navigate(`/confirm-reservation`, {replace: false});
        }
    }

    return (
        <div>
            <p style={{color: '#767676'}}>Ecran</p>
            {movie_hall_configuration && selectedSeats && (
                <>
                    <div className="d-grid hall-configuration">
                        {movie_hall_configuration}
                    </div>
                </>
            )}
            {selectedSeats && selectedSeats.length === 0 && (
                <Button id="continue_btn" className="continue_btn_disabled" disabled={true}
                        onClick={() => confirmSeats()}>Continue</Button>
            )}
            {selectedSeats && selectedSeats.length !== 0 && (
                <Button id="continue_btn" className="continue_btn" disabled={false}
                        onClick={() => confirmSeats()}>Continue</Button>
            )}
        </div>
    )
        ;
};

export default ReservationSelectSeats;
