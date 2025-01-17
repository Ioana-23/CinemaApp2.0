import axios from 'axios';
// import { authConfig, baseUrl, getLogger, withLogs } from '../core';
import { MovieScreeningProps } from './MovieScreeningProps';

const movieScreeningUrl = `http://localhost:8082/project/movie_screenings`;
const movieUrl = `http://localhost:8083/project/movies/movie`;

export const getMovieScreening = (): Promise<ResponseListMovieScreenings> => {
    return axios.get(movieScreeningUrl);
}

export const getMovieInfo = (id: number): Promise<Response> => {
    return axios.get(`${movieUrl}/${id}`);
}
export interface ResponseListMovieScreenings {
    responseObject: ListOfMovieScreenings,
    responseType: ResponseType,
    message: string
}

export interface Response {
    responseObject: MovieScreeningProps[],
    responseType: ResponseType,
    message: string
}

export interface ListOfMovieScreenings {
    totalPages: number,
    currentPage: number,
    movieScreeningDTOS: MovieScreeningProps[]
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
