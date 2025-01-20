import MovieScreeningCard from './MovieScreeningCard.tsx';
import {CardGroup, Pagination} from "react-bootstrap";
import React, {useContext, useEffect, useState} from "react";
import {MovieContext} from "./providers/MovieScreeningProvider.tsx";
import '../css/MovieScreeningList.css';
interface MovieScreeningListProps {
    // children: PropTypes.ReactNodeLike,
    dateToFilterBy: Date
}

const MovieScreeningList: React.FC<MovieScreeningListProps> = ({dateToFilterBy}) => {
    const {items, fetching, fetchingError, paginationFunction, currentPage, tab_no, total_pages, pagination_ribbon} = useContext(MovieContext);
    useEffect(() => {
        if (items) {
            // items.map(({times}, index) => console.log(`${index}:${times}\n`))
            console.log({items})
            // console.log({currentPage})
            // console.log({pagination_ribbon})
        }
    }, [items, currentPage, pagination_ribbon]);
    return (
        <div>
            {items && !items.find(item => item.movie == null) && pagination_ribbon && (
                <div>
                    <CardGroup className="flex-column gap-3">
                        {items
                            .map(({uuid, date, times, movieHall_uuid, movie}, index) =>
                                <MovieScreeningCard key={index} uuid={uuid} movie={movie}
                                                    movieHall_uuid={movieHall_uuid} date={date} times={times}/>
                            )}
                    </CardGroup>
                    <Pagination className="pagination-ribbon">
                        {pagination_ribbon}
                    </Pagination>
                </div>
            )}
            {items && items.length === 0 && (
                <p>There are no shows available for the date selected!</p>
            )}
        </div>
    );
};

export default MovieScreeningList;
