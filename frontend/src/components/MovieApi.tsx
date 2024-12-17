// import axios from 'axios';
// // import { authConfig, baseUrl, getLogger, withLogs } from '../core';
// import { MovieProps } from './MovieProps';
//
// const itemUrl = `http://localhost:8083/project/movies`;
//
// export const getItems = (): Promise<Response> => {
//     return axios.get(itemUrl);
// }
// export interface Response {
//     responseObject: MovieProps[],
//     responseType: ResponseType,
//     message: string
// }
//
// export enum ResponseType {
//     SUCCESS, ERROR
// }
//
// // export const createItem: (token: string, item: ItemProps) => Promise<ItemProps[]> = (token, item) => {
// //     return withLogs(axios.post(itemUrl, item, authConfig(token)), 'createItem');
// // }
// //
// // export const updateItem: (token: string, item: ItemProps) => Promise<ItemProps[]> = (token, item) => {
// //     return withLogs(axios.put(`${itemUrl}/${item._id}`, item, authConfig(token)), 'updateItem');
// // }
//
// interface MessageData {
//     type: string;
//     payload: MovieScreeningProps;
// }
// // export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
// //     const ws = new WebSocket(`ws://${baseUrl}`);
// //     ws.onopen = () => {
// //         log('web socket onopen');
// //         ws.send(JSON.stringify({ type: 'authorization', payload: { token } }));
// //     };
// //     ws.onclose = () => {
// //         log('web socket onclose');
// //     };
// //     ws.onerror = error => {
// //         log('web socket onerror', error);
// //     };
// //     ws.onmessage = messageEvent => {
// //         log('web socket onmessage');
// //         onMessage(JSON.parse(messageEvent.data));
// //     };
// //     return () => {
// //         ws.close();
// //     }
// // }
