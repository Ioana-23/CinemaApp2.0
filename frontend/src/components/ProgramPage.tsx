import {useEffect, useState} from "react";
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import {Col, Dropdown, DropdownButton, Nav} from "react-bootstrap";
import '../css/ProgramPage.css';
import MovieScreeningList from "./MovieScreeningList.tsx";
import {MovieScreeningProvider} from "./providers/MovieScreeningProvider.tsx";
import {usePreferences} from "./usePreferences.ts";

function getDates(year: number, month: number, day: number) {
    const currentDay = year ? new Date(year, month, day) : new Date();
    let currentDayOfTheWeek = year ? new Date(year, month, day) : new Date();
    const startOfWeek = new Date();
    const oneDayOffset = 86400000;
    startOfWeek.setDate(currentDay.getDate() - (currentDay.getDay() + 6) % 7)
    const dates = [];
    for (let i = 0; i < 7; i++) {
        // currentDayOfTheWeek.setDate(startOfWeek.getDate() + i);
        currentDayOfTheWeek = new Date(+startOfWeek + i*oneDayOffset)
        let month_aux: string = (currentDayOfTheWeek.getMonth() + 1).toString();
        let day_aux: string = currentDayOfTheWeek.getDate().toString();
        const year_aux: string = currentDayOfTheWeek.getFullYear().toString();
        let disabled = false;
        if (currentDay > currentDayOfTheWeek) {
            disabled = false;
        }
        if (month_aux.length === 1) {
            month_aux = `0${month_aux}`
        }
        if (day_aux.length === 1) {
            day_aux = `0${day_aux}`
        }
        dates.push({
            'date': `${day_aux}/${month_aux}/${year_aux}`,
            'disabled': disabled,
            'full_date': new Date(currentDayOfTheWeek)
        });
    }
    return dates;
}

export function transformStringToDate(dateAsString: string)
{
    const year = parseInt(dateAsString.split('/')[2])
    const month = parseInt(dateAsString.split('/')[1])
    const day = parseInt(dateAsString.split('/')[0])
    return new Date(year, month-1, day)
}
function ProgramPage() {
    const [daysOfTheCurrentWeek] = useState(getDates(2025, 0, 16));
    // const [daysOfTheCurrentWeek] = useState(getDates());
    const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
    // const currentDay = new Date().getDay();
    const currentDay = new Date(2025, 0, 16).getDay();
    // const [daySelected, setDaySelected] = useState<number>((currentDay + 6) % 7);
    const [daySelected, setDaySelected] = useState<number>(3);
    const [no_movies_per_page, setNo_movies_per_page] = useState<number>(0);
    const [movies_per_page] = useState<number[]>([1, 2, 3]);
    const {get, set} = usePreferences();
    useEffect(setMoviesPerPage, [no_movies_per_page]);
    useEffect(() => {
        if(daySelected)
        {
            const save_current_date = (async () => {
                await set('current_date', JSON.stringify(daysOfTheCurrentWeek[daySelected].full_date))
            })
            // save_current_date()
        }
    }, [daySelected]);

    function setMoviesPerPage() {
        let canceled = false;
        getOrSetMovies();
        return () => {
            canceled = true;
        }

        async function getOrSetMovies() {
            const saved_movies_per_page_json = await get('movies_per_page')
            const saved_movies_per_page = (saved_movies_per_page_json ? JSON.parse(saved_movies_per_page_json) : -1)
            console.log({saved_movies_per_page})
            if (saved_movies_per_page !== -1) {
                setNo_movies_per_page(saved_movies_per_page)
            }
        }
    }

    return (
        <Container id="day-selector">
            <div className="d-flex flex-row">
                <div className="d-flex flex-column day-navbar">
                    <Navbar sticky="top">
                        <Container className="d-flex justify-content-start">
                            <Col className="d-flex flex-column ">
                                <div className="d-flex flex-row">
                                    <DropdownButton
                                        size="sm"
                                        className="movie-per-page-selector"
                                        title={movies_per_page[no_movies_per_page]}>
                                        <Dropdown.Item onFocus={() => {
                                            setNo_movies_per_page(Math.min((no_movies_per_page + 1) % 3, (no_movies_per_page + 2) % 3))
                                            set('movies_per_page', JSON.stringify(Math.min((no_movies_per_page + 1) % 3, (no_movies_per_page + 2) % 3)))
                                            console.log(movies_per_page[Math.min((no_movies_per_page + 1) % 3, (no_movies_per_page + 2) % 3)])
                                        }}>{movies_per_page[Math.min((no_movies_per_page + 1) % 3, (no_movies_per_page + 2) % 3)]}</Dropdown.Item>
                                        <Dropdown.Item onFocus={() => {
                                            setNo_movies_per_page(Math.max((no_movies_per_page + 1) % 3, (no_movies_per_page + 2) % 3))
                                            set('movies_per_page', JSON.stringify(Math.max((no_movies_per_page + 1) % 3, (no_movies_per_page + 2) % 3)))
                                        }}>{movies_per_page[Math.max((no_movies_per_page + 1) % 3, (no_movies_per_page + 2) % 3)]}</Dropdown.Item>
                                    </DropdownButton>
                                    <p className="paragraph"> Movies per page</p>
                                </div>
                                {/*    /*<Nav defaultActiveKey={days[daySelected]} className="ml-5 border-0 flex-column"*/}
                                {/*           fill>*/}

                                {/*    <Nav.Link onFocus={() => {*/}
                                {/*        setDaySelected(1);*/}
                                {/*        console.log(daysOfTheCurrentWeek[1].full_date)*/}
                                {/*    }} eventKey={days[1]}*/}
                                {/*              disabled={daysOfTheCurrentWeek[1].disabled}>Monday {"\n"} {daysOfTheCurrentWeek[1].date} </Nav.Link>*/}
                                {/*    <Nav.Link onFocus={() => {*/}
                                {/*        setDaySelected(2);*/}
                                {/*        console.log(daysOfTheCurrentWeek[2].full_date)*/}
                                {/*    }} eventKey={days[2]}*/}
                                {/*              disabled={daysOfTheCurrentWeek[2].disabled}>Tuesday {"\n"} {daysOfTheCurrentWeek[2].date}</Nav.Link>*/}
                                {/*    <Nav.Link onFocus={() => {*/}
                                {/*        setDaySelected(3);*/}
                                {/*        console.log(daysOfTheCurrentWeek[3].full_date)*/}
                                {/*    }} eventKey={days[3]}*/}
                                {/*              disabled={daysOfTheCurrentWeek[3].disabled}>Wednesday {"\n"} {daysOfTheCurrentWeek[3].date}</Nav.Link>*/}
                                {/*    <Nav.Link onFocus={() => {*/}
                                {/*        setDaySelected(4);*/}
                                {/*        console.log(daysOfTheCurrentWeek[4].full_date)*/}
                                {/*    }} eventKey={days[4]}*/}
                                {/*              disabled={daysOfTheCurrentWeek[4].disabled}>Thursday {"\n"} {daysOfTheCurrentWeek[4].date}</Nav.Link>*/}
                                {/*    <Nav.Link onFocus={() => {*/}
                                {/*        setDaySelected(5);*/}
                                {/*        console.log(daysOfTheCurrentWeek[5].full_date)*/}
                                {/*    }} eventKey={days[5]}*/}
                                {/*              disabled={daysOfTheCurrentWeek[5].disabled}>Friday {"\n"} {daysOfTheCurrentWeek[5].date}</Nav.Link>*/}
                                {/*    <Nav.Link onFocus={() => {*/}
                                {/*        setDaySelected(6);*/}
                                {/*        console.log(daysOfTheCurrentWeek[6].full_date)*/}
                                {/*    }} eventKey={days[6]}*/}
                                {/*              disabled={daysOfTheCurrentWeek[6].disabled}>Saturday {"\n"} {daysOfTheCurrentWeek[6].date}</Nav.Link>*/}
                                {/*    <Nav.Link onFocus={() => {*/}
                                {/*        setDaySelected(7);*/}
                                {/*        console.log(daysOfTheCurrentWeek[7].full_date)*/}
                                {/*    }} eventKey={days[7]}*/}
                                {/*              disabled={daysOfTheCurrentWeek[7].disabled}>Sunday {"\n"} {daysOfTheCurrentWeek[7].date}</Nav.Link>*/}
                                {/*</Nav>*/}
                                <Nav defaultActiveKey={days[daySelected]} className="ml-5 border-0 flex-column"
                                     fill>
                                    <Nav.Link onFocus={() => {
                                        setDaySelected(0);
                                        console.log(daysOfTheCurrentWeek[0].full_date)
                                    }} eventKey={days[0]}
                                              disabled={daysOfTheCurrentWeek[0].disabled}>Monday {"\n"} {daysOfTheCurrentWeek[0].date} </Nav.Link>
                                    <Nav.Link onFocus={() => {
                                        setDaySelected(1);
                                        console.log(daysOfTheCurrentWeek[1].full_date)
                                    }} eventKey={days[1]}
                                              disabled={daysOfTheCurrentWeek[1].disabled}>Tuesday {"\n"} {daysOfTheCurrentWeek[1].date}</Nav.Link>
                                    <Nav.Link onFocus={() => {
                                        setDaySelected(2);
                                        console.log(daysOfTheCurrentWeek[2].full_date)
                                    }} eventKey={days[2]}
                                              disabled={daysOfTheCurrentWeek[2].disabled}>Wednesday {"\n"} {daysOfTheCurrentWeek[2].date}</Nav.Link>
                                    <Nav.Link onFocus={() => {
                                        setDaySelected(3);
                                        console.log(daysOfTheCurrentWeek[3].full_date)
                                    }} eventKey={days[3]}
                                              disabled={daysOfTheCurrentWeek[3].disabled}>Thursday {"\n"} {daysOfTheCurrentWeek[3].date}</Nav.Link>
                                    <Nav.Link onFocus={() => {
                                        setDaySelected(4);
                                        console.log(daysOfTheCurrentWeek[4].full_date)
                                    }} eventKey={days[4]}
                                              disabled={daysOfTheCurrentWeek[4].disabled}>Friday {"\n"} {daysOfTheCurrentWeek[4].date}</Nav.Link>
                                    <Nav.Link onFocus={() => {
                                        setDaySelected(5);
                                        console.log(daysOfTheCurrentWeek[5].full_date)
                                    }} eventKey={days[5]}
                                              disabled={daysOfTheCurrentWeek[5].disabled}>Saturday {"\n"} {daysOfTheCurrentWeek[5].date}</Nav.Link>
                                    <Nav.Link onFocus={() => {
                                        setDaySelected(6);
                                        console.log(daysOfTheCurrentWeek[6].full_date)
                                    }} eventKey={days[6]}
                                              disabled={daysOfTheCurrentWeek[6].disabled}>Sunday {"\n"} {daysOfTheCurrentWeek[6].date}</Nav.Link>
                                </Nav>
                            </Col>
                        </Container>
                    </Navbar>
                </div>
                <div className="d-flex flex-column movie-container">
                    {daySelected >= 0 && (
                        <MovieScreeningProvider currentDate={daysOfTheCurrentWeek[daySelected].full_date}
                                                number_movies_per_page={movies_per_page[no_movies_per_page]}>
                            <MovieScreeningList
                                dateToFilterBy={daysOfTheCurrentWeek[daySelected].full_date}></MovieScreeningList>
                        </MovieScreeningProvider>
                    )}
                </div>
            </div>
        </Container>
    );
}


export default ProgramPage;