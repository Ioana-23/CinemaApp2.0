import React from 'react';
import Card from "react-bootstrap/Card";
import {Image, Row} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {ReadMore} from "./ReadMore.tsx";
import {MovieScreeningProps} from "./MovieScreeningProps.tsx";

interface MovieScreeningPropsExt extends MovieScreeningProps {
    // onEdit: (_uuid: number) => void;
}

const MovieScreeningCard: React.FC<MovieScreeningPropsExt> = ({
                                                                  _uuids,
                                                                  movie,
                                                                  movieHall_uuid,
                                                                  datetime
                                                              }) => {


    return (
        <Card style={{width: '15rem', height: 'auto', padding: '0'}}>
            <Row style={{width: '100%', height: '100%'}}>
                <div style={{width: '25%', height: '100%'}}>
                    <Card.Img style={{width: '80%', height: '100%', objectFit: 'cover'}}
                              src="https://digitalreach.asia/wp-content/uploads/2021/11/placeholder-image.png"/>
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
                        {datetime && (
                            <div className="d-flex flex-row" style={{width: '100%', gap: '0.5rem'}}>
                                {datetime
                                    .map((date, index) =>
                                        <Button  key={_uuids[index]} style={{backgroundColor: '#f64b4b'}}
                                                className="border-0">{date.getHours()}:{date.getMinutes()}</Button>
                                    )}
                            </div>
                        )}
                    </Card.Body>
                </div>
            </Row>
        </Card>
    );
    //     <IonItem onClick={() => onEdit(_id)}>
    //         <IonCol size="1">
    //             <IonImg src={photo?.webviewPath}></IonImg>
    //         </IonCol>
    //         <IonCol>
    //             <IonTitle title="titlu">
    //                 <IonLabel>{title}</IonLabel>
    //             </IonTitle>
    //             <IonTitle title="data">
    //                 <IonLabel>{date.toString().substring(0,10)}</IonLabel>
    //             </IonTitle>
    //             <IonTitle title="review">
    //                 <IonLabel>{review}</IonLabel>
    //             </IonTitle>
    //             <IonTitle title="watched">
    //                 <IonLabel aria-disabled={true}>
    //                     <IonCheckbox aria-label="Label" checked={String(watched) == "true"}></IonCheckbox>
    //                 </IonLabel>
    //             </IonTitle>
    //         </IonCol>
    //     </IonItem>
    // );
};

export default MovieScreeningCard;
