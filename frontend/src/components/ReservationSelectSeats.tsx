import React, {useContext, useEffect, useState} from 'react';
import {Col, Row} from "react-bootstrap";
import {RouteComponentProps} from "react-router";
import {useSearchParams} from 'react-router-dom';
import {MovieContext} from "./providers/MovieScreeningProvider.tsx";
import '../css/ReservationSelectSeats.css';
import Button from "react-bootstrap/Button";
import {ReservationContext} from "./providers/ReservationProvider.tsx";

interface ReservationSelectSeatsExt extends RouteComponentProps {
    uuid?: string;
}

const ReservationSelectSeats: React.FC<ReservationSelectSeatsExt> = ({history, match}) => {
    const {
        allItems
    } = useContext(MovieContext);
    const {selectedSeats, getSeats, movie_hall_configuration} = useContext(ReservationContext);
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
    useEffect(() => {
        getSeats!(movie_hall_id)
    }, [movie_hall_id, allItems]);

    // function configureHall() {
    //     try {
    //         if (seats && seats.length > 0) {
    //             if (selectedSeats.length > 0) {
    //                 document.getElementById("continue_btn")!.disabled = false;
    //                 document.getElementById("continue_btn")!.className = "continue_btn";
    //             } else {
    //                 document.getElementById("continue_btn")!.disabled = true;
    //                 document.getElementById("continue_btn")!.className = "continue_btn_disabled";
    //             }
    //             const config = [];
    //             const row_no = seats[seats.length - 1].row_number;
    //             const col_no = seats[seats.length - 1].seat_number;
    //             const alphabet: string[] = "abcdefghijklmnopqrstuvw".toUpperCase().split('')
    //             let count = 0;
    //             for (let i = 0; i < row_no; i++) {
    //                 const row = [];
    //                 for (let j = 0; j < col_no; j++) {
    //                     const height = 100 / row_no;
    //                     if (seats[count].available) {
    //                         if (selectedSeats.length != 0 && selectedSeats.findIndex(seat => seat.row_number == seats[count].row_number && seat.seat_number == seats[count].seat_number) !== -1) {
    //                             console.log('heree')
    //                             row.push(<Col style={{paddingLeft: '1rem', alignItems: 'center'}}
    //                                           className="d-flex flex-column seat-box" key={seats[count].uuid}>
    //                                 <a
    //                                     id={count.toString()}
    //                                     className="seat-link-selected" data-key={seats[count].id}
    //                                     onClick={(event) => {
    //                                         const value = parseInt(event.target.attributes[2].nodeValue);
    //                                         const id = event.target.id
    //                                         handleClickSeats(value, id)
    //                                     }}
    //                                     style={{
    //                                         height: `${height * 4}%`,
    //                                         width: `${height * 4}%`
    //                                     }}>{alphabet[i]}/{j}</a>
    //                             </Col>)
    //                         } else {
    //                             row.push(<Col style={{paddingLeft: '1rem', alignItems: 'center'}}
    //                                           className="d-flex flex-column seat-box" key={seats[count].uuid}>
    //                                 <a
    //                                     id={count.toString()}
    //                                     className="seat-link" data-key={count} onClick={(event) => {
    //                                     const value = parseInt(event.target.attributes[2].nodeValue);
    //                                     const id = event.target.id
    //                                     handleClickSeats(value, id)
    //                                 }}
    //                                     style={{
    //                                         height: `${height * 4}%`,
    //                                         width: `${height * 4}%`
    //                                     }}>{alphabet[i]}/{j}</a>
    //                             </Col>)
    //                         }
    //                     } else {
    //                         row.push(<Col style={{paddingLeft: '1rem', alignItems: 'center'}}
    //                                       className="d-flex flex-column seat-box" key={seats[count].uuid}> <a
    //                             className="seat-link-disabled disabled"
    //                             data-key={count}
    //                             style={{
    //                                 height: `${height * 4}%`,
    //                                 width: `${height * 4}%`
    //                             }}>{alphabet[i]}/{j}</a>
    //                         </Col>)
    //                     }
    //                     count++;
    //                 }
    //                 config.push(<Row className="d-flex flex-row">{row}</Row>)
    //             }
    //             setHall_configuration(config)
    //         }
    //     } catch (error) {
    //         console.log({error})
    //     }
    // }

    // const handleContinueWithPayment = () => {
    //     const setInLocalStorage = (async () => await set('selected_seats', JSON.stringify([])));
    //     setInLocalStorage();
    //     setSelectedSeats([]);
    // }

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
                    onClick={() => handleContinueWithPayment()}>Continue</Button>
            )}
            {selectedSeats && selectedSeats.length !== 0 && (
                <Button id="continue_btn" className="continue_btn" disabled={false}
                        onClick={() => handleContinueWithPayment()}>Continue</Button>
            )}
        </div>
    )
        ;
};

export default ReservationSelectSeats;
