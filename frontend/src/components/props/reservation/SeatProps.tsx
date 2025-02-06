export interface MovieHallProps{
    id: number,
    uuid: number
}

export interface SeatProps {
    uuid: number,
    available: boolean,
    row_number: number,
    seat_number: number
}