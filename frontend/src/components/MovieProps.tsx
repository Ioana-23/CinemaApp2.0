export enum Gender {
    OTHER, FEMALE, MALE, NON_BINARY
}

export interface ActorProps{
    name: string;
    gender: Gender;
    _uuid: number;
}

export interface GenreProps{
    name: string;
    _uuid: number;
}

export interface MovieProps {
    _uuid: number;
    title: string;
    date: Date;
    actors: ActorProps[];
    overview: string;
    adult: boolean;
    language: string;
    genres: GenreProps[];
}
