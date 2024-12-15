import {MovieProps} from "./MovieProps.tsx";

export interface MovieScreeningProps{
    _uuids: number[],
    movie: MovieProps;
    datetime: Date[];
    movieHall_uuid: number;
}