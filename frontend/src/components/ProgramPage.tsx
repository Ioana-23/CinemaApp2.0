import {useState} from "react";
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import {Col, Nav, Row} from "react-bootstrap";
import '../css/ProgramPage.css';
import MovieScreeningList from "./MovieScreeningList.tsx";

function getDates() {
    const currentDay = new Date();
    const currentDayOfTheWeek = new Date();
    const dates = [];
    for (let i = 0; i < 7; i++) {
        currentDayOfTheWeek.setDate(currentDay.getDate() - (currentDay.getDay() + 6) % 7 + i);
        const month = currentDayOfTheWeek.getMonth() + 1;
        const day = currentDayOfTheWeek.getDate();
        const year = currentDayOfTheWeek.getFullYear();
        let disabled = false;
        if (currentDay > currentDayOfTheWeek) {
            disabled = true;
        }
        dates.push({'date': `${day}/${month}/${year}`, 'disabled': disabled});
    }
    return dates;
}

function ProgramPage() {
    const [daysOfTheCurrentWeek] = useState(getDates());
    const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
    const currentDay = new Date().getDay();
    const [daySelected, setDaySelected] = useState<number>((currentDay + 6) % 7);
    return (
        <Container id="day-selector">
            <div className="d-flex flex-row">
                <div className="d-flex flex-column day-navbar">
                    <Navbar>
                        <Container className="d-flex justify-content-start">
                            <Nav defaultActiveKey={days[(currentDay + 6) % 7]} className="ml-5 border-0 flex-column" fill>

                                <Nav.Link onFocus={() => {
                                    setDaySelected(0);
                                    console.log(0)
                                }} eventKey={days[0]}
                                          disabled={daysOfTheCurrentWeek[0].disabled}>Monday {"\n"} {daysOfTheCurrentWeek[0].date} </Nav.Link>
                                <Nav.Link onFocus={() => {
                                    setDaySelected(1);
                                    console.log(1)
                                }} eventKey={days[1]}
                                          disabled={daysOfTheCurrentWeek[1].disabled}>Tuesday {"\n"} {daysOfTheCurrentWeek[1].date}</Nav.Link>
                                <Nav.Link onFocus={() => {
                                    setDaySelected(2);
                                    console.log(2)
                                }} eventKey={days[2]}
                                          disabled={daysOfTheCurrentWeek[2].disabled}>Wednesday {"\n"} {daysOfTheCurrentWeek[2].date}</Nav.Link>
                                <Nav.Link onFocus={() => {
                                    setDaySelected(3);
                                    console.log(3)
                                }} eventKey={days[3]}
                                          disabled={daysOfTheCurrentWeek[3].disabled}>Thursday {"\n"} {daysOfTheCurrentWeek[3].date}</Nav.Link>
                                <Nav.Link onFocus={() => {
                                    setDaySelected(4);
                                    console.log(4)
                                }} eventKey={days[4]}
                                          disabled={daysOfTheCurrentWeek[4].disabled}>Friday {"\n"} {daysOfTheCurrentWeek[4].date}</Nav.Link>
                                <Nav.Link onFocus={() => {
                                    setDaySelected(5);
                                    console.log(5)
                                }} eventKey={days[5]}
                                          disabled={daysOfTheCurrentWeek[5].disabled}>Saturday {"\n"} {daysOfTheCurrentWeek[5].date}</Nav.Link>
                                <Nav.Link onFocus={() => {
                                    setDaySelected(6);
                                    console.log(6)
                                }} eventKey={days[6]}
                                          disabled={daysOfTheCurrentWeek[6].disabled}>Sunday {"\n"} {daysOfTheCurrentWeek[6].date}</Nav.Link>
                            </Nav>
                        </Container>
                    </Navbar>
                </div>
                <div className="d-flex flex-column movie-container">
                    <MovieScreeningList></MovieScreeningList>
                </div>
            </div>
        </Container>
    );
}


export default ProgramPage;