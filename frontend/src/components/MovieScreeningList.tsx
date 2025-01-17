import MovieScreeningCard from './MovieScreeningCard.tsx';
import {CardGroup, Pagination} from "react-bootstrap";
import React, {useContext, useEffect} from "react";
import {MovieContext} from "./MovieScreeningProvider.tsx";

interface MovieScreeningListProps {
    // children: PropTypes.ReactNodeLike,
    dateToFilterBy: Date
}

const MovieScreeningList: React.FC<MovieScreeningListProps> = ({dateToFilterBy}) => {
    const {items, fetching, fetchingError, paginationFunction, currentPage} = useContext(MovieContext);
    useEffect(() => {
        if (items) {
            // items.map(({times}, index) => console.log(`${index}:${times}\n`))
            console.log({items})
            console.log({currentPage})
        }
    }, [items, currentPage]);
    return (
        <div>
            {items && !items.find(item => item.movie == null) && (
                <>
                    <CardGroup className="flex-column gap-3">
                        {items
                            .map(({uuid, date, times, movieHall_uuid, movie}, index) =>
                                <MovieScreeningCard key={index} uuid={uuid} movie={movie}
                                                    movieHall_uuid={movieHall_uuid} date={date} times={times}/>
                            )}
                    </CardGroup>
                    <Pagination>
                        <Pagination.Item key="0" active={currentPage === 0}
                                         onClick={() => paginationFunction?.(0)}>1</Pagination.Item>
                        <Pagination.Item key="1" active={currentPage === 1}
                                         onClick={() => paginationFunction?.(1)}>2</Pagination.Item>
                    </Pagination>
                </>
            )}
        </div>
    );
};

export default MovieScreeningList;
