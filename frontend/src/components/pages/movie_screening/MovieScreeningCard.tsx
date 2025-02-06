import React, {useState} from 'react';
import Card from "react-bootstrap/Card";
import {Image, Row} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {ReadMore} from "../../ReadMore.tsx";
import {MovieScreeningProps} from "../../props/movie_screening/MovieScreeningProps.tsx";
import {useNavigate} from "react-router-dom";
import '../../../css/MovieScreeningCard.css';
import {usePreferences} from "../../usePreferences.ts";

interface MovieScreeningPropsExt extends MovieScreeningProps {
    // onEdit: (_uuid: number) => void;
}

const MovieScreeningCard: React.FC<MovieScreeningPropsExt> = ({
                                                                  uuid,
                                                                  movie,
                                                                  movieHall_uuid,
                                                                  date,
                                                                  times,
                                                              }) => {
    const [imgSrc, setImgSrc] = useState(movie.poster_path || 'https://digitalreach.asia/wp-content/uploads/2021/11/placeholder-image.png');
    const {get, remove} = usePreferences();
    const navigate = useNavigate();
    const handleMenuClick = ((uuid: number) => {
            const setInLocalStorage = (async () => {
                await remove('reservation')
            });
            setInLocalStorage();
            navigate(`/reserve?uuid=${uuid}`, {replace: false});
        }
    );

    return (
        <Card style={{height: 'auto', padding: '0'}} className="card-movie-screening">
            <Row style={{width: '100%', height: '100%'}}>
                <div style={{width: '25%', height: '100%'}}>
                    <Card.Img className="poster" id="image" style={{
                        width: '80%',
                        height: '100%',
                        objectFit: 'cover',
                        backgroundImage: 'url(\'https://digitalreach.asia/wp-content/uploads/2021/11/placeholder-image.png\')'
                    }}
                              onError={() => setImgSrc("https://digitalreach.asia/wp-content/uploads/2021/11/placeholder-image.png")}
                        // src="https://image.tmdb.org/t/p/original/aosm8NMQ3UyoBVpSxyimorCQykC.jpg"
                              src={movie.poster_path || 'https://digitalreach.asia/wp-content/uploads/2021/11/placeholder-image.png'}
                    />
                </div>
                <div style={{width: '75%', height: '100%'}}>
                    <Card.Body style={{width: '100%', height: '100%'}}>
                        <Card.Title style={{textAlign: 'start'}}>{movie.title}</Card.Title>
                        <Card.Text style={{textAlign: 'start'}}>
                            <ReadMore text={movie.overview}/>
                        </Card.Text>
                        {movie.genres && (
                            <div className="d-flex flex-row" style={{width: '100%', gap: '0.5rem'}}>
                                {movie.genres
                                    // .filter(item => item.title.toString().includes(searchMovie) && ( filter!== "" ? item.review >= Number(filter) && item.review < Number(filter) + 1 : true))
                                    .map(({_uuid, name}) =>
                                        <div className="d-flex flex-row" key={_uuid}>
                                            <div className="d-flex flex-column">
                                                <Image src="src/images/Tag.png"/>
                                            </div>
                                            <div className="d-flex flex-column">
                                                <p style={{textAlign: 'start'}}>{name}</p>
                                            </div>
                                        </div>
                                    )}
                            </div>
                        )}
                        {times && (
                            <div className="d-flex flex-row" style={{width: '100%', gap: '0.5rem'}}>
                                {times
                                    .sort((time1, time2) =>
                                        new Date().setHours(parseInt(time1.split(":")[0])) < new Date().setHours(parseInt(time2.split(":")[0])) ? -1 :
                                            new Date().setHours(parseInt(time1.split(":")[0])) == new Date().setHours(parseInt(time2.split(":")[0])) ?
                                                new Date().setMinutes(parseInt(time1.split(":")[1])) < new Date().setMinutes(parseInt(time2.split(":")[1])) ? -1 : 1 : -1)
                                    .map((date, index) =>
                                        <Button key={uuid[index]} style={{backgroundColor: '#f64b4b'}}
                                                className="border-0 btn reserve-button"
                                                onClick={() => handleMenuClick(uuid[index])}>{date.split(" ")[1]}</Button>
                                    )}
                            </div>
                        )}
                    </Card.Body>
                </div>
            </Row>
        </Card>
    );
};

export default MovieScreeningCard;
