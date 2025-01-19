export interface MovieHallProps{
    id: number,
    uuid: number
}

export interface SeatProps {
    id: number,
    uuid: number,
    available: boolean,
    row_number: number,
    seat_number: number,
    movieHall: MovieHallProps
}