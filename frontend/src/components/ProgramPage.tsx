import {useState} from "react";
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import {Nav} from "react-bootstrap";
import '../css/ProgramPage.css';
import MovieScreeningList from "./MovieScreeningList.tsx";
import {MovieScreeningProvider} from "./MovieScreeningProvider.tsx";

function getDates() {
    const currentDay = new Date();
    const currentDayOfTheWeek = new Date();
    const dates = [];
    for (let i = 0; i < 7; i++) {
        currentDayOfTheWeek.setDate(currentDay.getDate() - (currentDay.getDay() + 6) % 7 + i);
        let month: string = (currentDayOfTheWeek.getMonth() + 1).toString();
        let day: string = currentDayOfTheWeek.getDate().toString();
        const year: string = currentDayOfTheWeek.getFullYear().toString();
        let disabled = false;
        if (currentDay > currentDayOfTheWeek) {
            disabled = false;
        }
        if(month.length === 1)
        {
            month = `0${month}`
        }
        if(day.length === 1)
        {
            day = `0${day}`
        }
        dates.push({
            'date': `${day}/${month}/${year}`,
            'disabled': disabled,
            'full_date': new Date(currentDayOfTheWeek)
        });
    }
    return dates;
}

function ProgramPage() {
    const [daysOfTheCurrentWeek] = useState(getDates());
    const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
    const currentDay = new Date().getDay();
    // const [daySelected, setDaySelected] = useState<number>((currentDay + 6) % 7);
    const [daySelected, setDaySelected] = useState<number>(3);
    return (
        <Container id="day-selector">
            <div className="d-flex flex-row">
                <div className="d-flex flex-column day-navbar">
                    <Navbar sticky="top">
                        <Container className="d-flex justify-content-start">
                            {/*<Nav defaultActiveKey={days[(currentDay + 6) % 7]} className="ml-5 border-0 flex-column"*/}
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
                        </Container>
                    </Navbar>
                </div>
                <div className="d-flex flex-column movie-container">
                    <MovieScreeningProvider currentDate={daysOfTheCurrentWeek[daySelected].full_date}>
                        <MovieScreeningList dateToFilterBy={daysOfTheCurrentWeek[daySelected].full_date}></MovieScreeningList>
                    </MovieScreeningProvider>
                </div>
            </div>
        </Container>
    );
}


export default ProgramPage;