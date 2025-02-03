import axios from 'axios';
// import { authConfig, baseUrl, getLogger, withLogs } from '../core';
import {MovieScreeningProps} from '../../props/movie_screening/MovieScreeningProps.tsx';
import {SeatProps} from "../../props/reservation/SeatProps.tsx";

const movieHallsUrl = `http://localhost:8082/project/movie_halls`;

export const getMovieHallSeats = (uuid: number): Promise<ResponseListSeat> => {
    return axios.get(`${movieHallsUrl}/movie_hall/${uuid}/seats`);
}

export interface ResponseListSeat {
    responseObject: SeatProps[],
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

// export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
//     const ws = new WebSocket(`ws://${baseUrl}`);
//     ws.onopen = () => {
//         log('web socket onopen');
//         ws.send(JSON.stringify({ type: 'authorization', payload: { token } }));
//     };
//     ws.onclose = () => {
//         log('web socket onclose');
//     };
//     ws.onerror = error => {
//         log('web socket onerror', error);
//     };
//     ws.onmessage = messageEvent => {
//         log('web socket onmessage');
//         onMessage(JSON.parse(messageEvent.data));
//     };
//     return () => {
//         ws.close();
//     }
// }
