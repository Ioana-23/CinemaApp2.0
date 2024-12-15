// import { RouteComponentProps } from 'react-router';
import MovieScreeningCard from './MovieScreeningCard.tsx';
// import {initialStateItems, ItemContext, ItemsState, NUMBER_OF_MOVIES_PER_PAGE} from './ItemProvider';
// import { useAppState } from './useAppState';
// import { useNetwork } from './useNetwork';
// import {Preferences} from "@capacitor/preferences";
// import {AuthContext, AuthState, initialState} from "../auth";
import {MovieProps} from "./MovieProps.tsx";
import {CardGroup} from "react-bootstrap";
import {MovieScreeningProps} from "./MovieScreeningProps.tsx";
// import axios from "axios";
// import {usePhotos} from "./usePhotos";

// const MovieScreeningList: React.FC<RouteComponentProps> = ({ history }) => {
function MovieScreeningList() {
    const items: MovieScreeningProps[] = [{
        _uuids: [1, 2], movie: {
            _uuid: 1,
            title: 'We live in time', date: new Date("2024-10-11"),
            actors: [],
            overview: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sit amet faucibus enim. Integer luctus tellus quis lectus viverra dictum. Sed ac maximus libero, a feugiat dolor. Sed volutpat eros quam, nec sagittis nunc condimentum ac. Vivamus interdum enim sed sagittis accumsan. Curabitur pellentesque mattis commodo. Nam sagittis, dolor nec lacinia sagittis, dolor est dignissim eros, blandit sagittis purus velit ut nunc. Sed congue tellus non sagittis mollis. Fusce sollicitudin mi massa, nec luctus ligula scelerisque id. Sed velit elit, malesuada et sem quis, pharetra bibendum risus. Nullam nec imperdiet dolor. Donec tempor augue eu lorem interdum venenatis. Duis tristique imperdiet leo sit amet tristique. Curabitur augue dolor, mattis et elementum vel, scelerisque vel purus. Proin a tortor quis tortor auctor facilisis quis vitae turpis. ',
            adult: false,
            language: '',
            genres: [{_uuid: 1, name: 'lorem'}, {_uuid: 2, name: 'ipsum'}]
        },
        datetime: [new Date(), new Date()], movieHall_uuid: 1
}]
    ;
    // const { items, fetching, fetchingError, paginationFunction, setItems, reviews} = useContext(ItemContext);
    // const { logout} = useContext(AuthContext);
    // const [disableInfiniteScroll, setDisableInfiniteScroll] = useState<boolean>(false)
    // const [searchMovie, setSearchMovie] = useState<string>('');
    // const [filter, setFilter] = useState<string>('');
    // const { appState } = useAppState();
    // const { networkStatus } = useNetwork();
    // log('render', fetching);
    // async function fetchData() {
    //     if(items)
    //     {
    //         paginationFunction?.(items[items.length-1]).then((result) => {
    //             if(result)
    //             {
    //                 if (result && result.length)
    //                 {
    //                     setDisableInfiniteScroll(result.length < NUMBER_OF_MOVIES_PER_PAGE);
    //                 }
    //                 else
    //                 {
    //                     setDisableInfiniteScroll(true);
    //                 }
    //                 const itemsFinal: ItemProps[] = [];
    //                 for(let i = 0; i < items.length; i++)
    //                 {
    //                     itemsFinal.push(items[i]);
    //                 }
    //                 for(let i = 0; i < result.length; i++)
    //                 {
    //                     itemsFinal.push(result[i]);
    //                 }
    //                 setItems?.(itemsFinal);
    //             }
    //         });
    //     }
    // }
    // async function searchNext($event: CustomEvent<void>) {
    //     fetchData();
    //     await ($event.target as HTMLIonInfiniteScrollElement).complete();
    // }

    return (
        <div>
            {items && (
                <CardGroup>
                    {items
                        // .filter(item => item.title.toString().includes(searchMovie) && ( filter!== "" ? item.review >= Number(filter) && item.review < Number(filter) + 1 : true))
                        .map(({_uuids, movie, datetime, movieHall_uuid}, index) =>
                            <MovieScreeningCard key={index} _uuids={_uuids} movie={movie} movieHall_uuid={movieHall_uuid} datetime={datetime}/>
                        )}
                </CardGroup>
            )}
        </div>
    );
};

export default MovieScreeningList;
