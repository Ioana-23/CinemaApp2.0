import axios from 'axios';
// import { authConfig, baseUrl, getLogger, withLogs } from '../core';
import { MovieScreeningProps } from './MovieScreeningProps';

const itemUrl = `http://localhost:8082/project/movie_screenings`;

export const getItems = (date: string): Promise<Response> => {
    return axios.get(`${itemUrl}/${date}`);
}
export interface Response {
    responseObject: MovieScreeningProps[],
    responseType: ResponseType,
    message: string
}

export enum ResponseType {
    SUCCESS, ERROR
}

// export const createItem: (token: string, item: ItemProps) => Promise<ItemProps[]> = (token, item) => {
//     return withLogs(axios.post(itemUrl, item, authConfig(token)), 'createItem');
// }
//
// export const updateItem: (token: string, item: ItemProps) => Promise<ItemProps[]> = (token, item) => {
//     return withLogs(axios.put(`${itemUrl}/${item._id}`, item, authConfig(token)), 'updateItem');
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
