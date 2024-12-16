// import { RouteComponentProps } from 'react-router';
import { RouteComponentProps } from 'react-router';
import MovieScreeningCard from './MovieScreeningCard.tsx';
// import {initialStateItems, ItemContext, ItemsState, NUMBER_OF_MOVIES_PER_PAGE} from './ItemProvider';
// import { useAppState } from './useAppState';
// import { useNetwork } from './useNetwork';
// import {Preferences} from "@capacitor/preferences";
// import {AuthContext, AuthState, initialState} from "../auth";
import {CardGroup} from "react-bootstrap";
import React, {useContext, useEffect} from "react";
import {MovieContext} from "./MovieScreeningProvider.tsx";
import {ActorProps, GenreProps} from "./MovieProps.tsx";
// import axios from "axios";
// import {usePhotos} from "./usePhotos";

// const MovieScreeningList: React.FC<RouteComponentProps> = ({ history }) => {
const MovieScreeningList: React.FC<RouteComponentProps> = ({history}, dateToFilterBy) => {
    // const items: MovieScreeningProps[] = [{
    //         _uuids: [1, 2], movie: {
    //             _uuid: 1,
    //             title: 'We live in time', date: new Date(),
    //             actors: [],
    //             overview: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sit amet faucibus enim. Integer luctus tellus quis lectus viverra dictum. Sed ac maximus libero, a feugiat dolor. Sed volutpat eros quam, nec sagittis nunc condimentum ac. Vivamus interdum enim sed sagittis accumsan. Curabitur pellentesque mattis commodo. Nam sagittis, dolor nec lacinia sagittis, dolor est dignissim eros, blandit sagittis purus velit ut nunc. Sed congue tellus non sagittis mollis. Fusce sollicitudin mi massa, nec luctus ligula scelerisque id. Sed velit elit, malesuada et sem quis, pharetra bibendum risus. Nullam nec imperdiet dolor. Donec tempor augue eu lorem interdum venenatis. Duis tristique imperdiet leo sit amet tristique. Curabitur augue dolor, mattis et elementum vel, scelerisque vel purus. Proin a tortor quis tortor auctor facilisis quis vitae turpis. ',
    //             adult: false,
    //             language: '',
    //             genres: [{_uuid: 1, name: 'lorem'}, {_uuid: 2, name: 'ipsum'}]
    //         },
    //         date: new Date(),
    //         times: [new Date(), new Date()], movieHall_uuid: 1
    //     }];
    const {items, fetching, fetchingError, paginationFunction, setItems} = useContext(MovieContext);
    console.log({items})
    return (
        <div>
            {items && (
                <CardGroup>
                    {items
                        // .filter(movie_screening => movie_screening.date.getFullYear() === dateToFilterBy.getFullYear() && movie_screening.date.getMonth() === dateToFilterBy.getMonth() && movie_screening.date.getDate() === dateToFilterBy.getDate())
                        .map(({uuid, date, times, movieHall_uuid}, index) =>
                            <MovieScreeningCard key={index} uuid={uuid} movie={{_uuid: 1, title: 'Alabala', date: new Date(), actors: [], overview: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sit amet faucibus enim. Integer luctus tellus quis lectus viverra dictum. Sed ac maximus libero, a feugiat dolor. Sed volutpat eros quam, nec sagittis nunc condimentum ac. Vivamus interdum enim sed sagittis accumsan. Curabitur pellentesque mattis commodo. Nam sagittis, dolor nec lacinia sagittis, dolor est dignissim eros, blandit sagittis purus velit ut nunc. Sed congue tellus non sagittis mollis. Fusce sollicitudin mi massa, nec luctus ligula scelerisque id. Sed velit elit, malesuada et sem quis, pharetra bibendum risus. Nullam nec imperdiet dolor. Donec tempor augue eu lorem interdum venenatis. Duis tristique imperdiet leo sit amet tristique. Curabitur augue dolor, mattis et elementum vel, scelerisque vel purus. Proin a tortor quis tortor auctor facilisis quis vitae turpis. ', adult: false, language: 'end', genres: [] }}
                                                movieHall_uuid={movieHall_uuid} date={date} times={times}/>
                        )}
                </CardGroup>
            )}
        </div>
    );
};

export default MovieScreeningList;
