import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import HomePage from "./HomePage.tsx";
import ProgramPage from "./ProgramPage.tsx";
import '../css/Header.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import {MovieScreeningProvider} from "./providers/MovieScreeningProvider.tsx";
import ReservationSelectSeats from "./ReservationSelectSeats.tsx";
import ConfirmReservation from "./ConfirmReservation.tsx";
import {ReservationProvider} from "./providers/ReservationProvider.tsx";

function Header() {
    return (
        <div id="header">
            <Navbar>
                <Navbar.Brand href="home">Cinema App</Navbar.Brand>
                <Container className="d-flex justify-content-end sign-in-up-container">
                    <Button className="log-in-button">Log in</Button>
                    <Button className="sign-up-container">Sign up</Button>
                </Container>
            </Navbar>
            <MovieScreeningProvider number_movies_per_page={0}>
                <ReservationProvider>
                    <BrowserRouter>
                        <Routes>
                            <Route path="/" element={<>
                                <Tabs defaultActiveKey="program"
                                      className="mb-3 nav-tabs navigation-container justify-content-end border-0"
                                      fill>
                                    <Tab eventKey="home" title="Home">
                                        {/*<Route>*/}
                                        <HomePage></HomePage>
                                        {/*</Route>*/}
                                    </Tab>
                                    <Tab eventKey="program" title="Program">
                                        <ProgramPage></ProgramPage>
                                    </Tab>
                                    <Tab eventKey="movies" title="Movies">
                                        Tab content for Contact
                                    </Tab><Tab eventKey="label" title="Label" disabled className="px-5 border-0">
                                    Tab content for Contact
                                </Tab>
                                </Tabs>
                            </>}/>
                            <Route path="/reserve/" render={props => {
                                return <ReservationSelectSeats {...props} />;
                            }} element={<ReservationSelectSeats/>}/>
                            <Route path="/confirm-reservation/" render={props => {
                                return <ConfirmReservation {...props} />;
                            }} element={<ConfirmReservation/>}/>
                        </Routes>
                    </BrowserRouter>
                </ReservationProvider>
            </MovieScreeningProvider>
        </div>
    );
}

export default Header;