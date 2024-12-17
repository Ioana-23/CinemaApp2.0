import {RouteComponentProps} from 'react-router';
import MovieScreeningCard from './MovieScreeningCard.tsx';
import {CardGroup, Col, Row} from "react-bootstrap";
import React, {useContext, useEffect} from "react";
import {MovieContext} from "./MovieScreeningProvider.tsx";
import PropTypes from "prop-types";

interface MovieScreeningListProps {
    // children: PropTypes.ReactNodeLike,
    dateToFilterBy: Date
}

const MovieScreeningList: React.FC<MovieScreeningListProps> = ({dateToFilterBy}) => {
    const {items, fetching, fetchingError, paginationFunction} = useContext(MovieContext);

    return (
        <div>
            {items && !items.find(item => item.movie == null) && (
                <CardGroup className="flex-column gap-3">
                    {items
                        .filter(movie_screening => parseInt(movie_screening.date.toString().split('/')[2]) === parseInt(dateToFilterBy.getFullYear().toString()) && parseInt(movie_screening.date.toString().split('/')[1]) - 1 === parseInt(dateToFilterBy.getMonth().toString()) && parseInt(movie_screening.date.toString().split('/')[0]) === parseInt(dateToFilterBy.getDate().toString()))
                        .map(({uuid, date, times, movieHall_uuid, movie}, index) =>
                            <MovieScreeningCard key={index} uuid={uuid} movie={movie}
                                                movieHall_uuid={movieHall_uuid} date={date} times={times}/>
                        )}
                </CardGroup>
            )}
        </div>
    );
};

export default MovieScreeningList;
