import {UserProps} from "../../UserProps.tsx";
import {MovieScreeningProps} from "../movie_screening/MovieScreeningProps.tsx";
import {SeatProps} from "./SeatProps.tsx";

export interface ReservationProps{
    uuid?: number,
    movie_screening?: MovieScreeningProps;
    movie_screening_uuid: number,
    user: UserProps;
    tickets: SeatProps[]
}

