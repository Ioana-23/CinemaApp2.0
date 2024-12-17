import {MovieProps} from "./MovieProps.tsx";

export interface MovieScreeningProps{
    uuid: number[],
    movie: MovieProps;
    date: Date;
    movie_uuid: number;
    times: string[];
    movieHall_uuid: number[];
}

