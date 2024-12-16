import {MovieProps} from "./MovieProps.tsx";

export interface MovieScreeningProps{
    uuid: number[],
    movie: MovieProps;
    date: Date;
    times: string[];
    movieHall_uuid: number[];
}

