import axios from 'axios';
// import { authConfig, baseUrl, getLogger, withLogs } from '../core';
import {MovieScreeningProps} from '../../props/movie_screening/MovieScreeningProps.tsx';
import {SeatProps} from "../../props/reservation/SeatProps.tsx";
import {ReservationProps} from "../../props/reservation/ReservationProps.tsx";

const reservationUrl = `http://localhost:8081/project/reservations`;

export const saveReservation = (reservation: ReservationProps): Promise<ResponseSaveReservation> => {
    return axios.post(`${reservationUrl}/reservation`, reservation);
}

export interface ResponseSaveReservation {
    responseObject: ReservationProps,
    responseType: ResponseType,
    message: string
}


export enum ResponseType {
    SUCCESS, ERROR
}

// export const createItem: (token: string, item: ItemProps) => Promise<ItemProps[]> = (token, item) => {
//     return withLogs(axios.post(movieScreeningUrl, item, authConfig(token)), 'createItem');
// }
//
// export const updateItem: (token: string, item: ItemProps) => Promise<ItemProps[]> = (token, item) => {
//     return withLogs(axios.put(`${movieScreeningUrl}/${item._id}`, item, authConfig(token)), 'updateItem');
// }

interface MessageData {
    type: string;
    payload: MovieScreeningProps;
}

