import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import React, {Fragment} from "react";
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import HomePage from "./HomePage.tsx";
import ProgramPage from "./ProgramPage.tsx";
import '../css/Header.css';
import {Redirect, Route, Router} from "react-router";
import {BrowserRouter, Routes} from "react-router-dom";
import ReservationSelectSeats from "./ReservationSelectSeats.tsx";
import {PrivateRoute} from "./PrivateRoute.tsx";
import {MovieScreeningProvider} from "./MovieScreeningProvider.tsx";

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
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<><Tabs defaultActiveKey="program"
                                                         className="mb-3 nav-tabs navigation-container justify-content-end border-0"
                                                         fill>
                            <Tab eventKey="home" title="Home">
                                {/*<Route>*/}
                                <HomePage></HomePage>
                                {/*</Route>*/}
                            </Tab>
                            <Tab eventKey="program" title="Program">
                                <ProgramPage></ProgramPage>
                            </Tab><Tab eventKey="movies" title="Movies">
                            Tab content for Contact
                        </Tab><Tab eventKey="label" title="Label" disabled className="px-5 border-0">
                            Tab content for Contact
                        </Tab>
                        </Tabs>
                        </>}/>
                        <Route path="/reserve/" render={props => {return <ReservationSelectSeats {...props} />;}} element={<ReservationSelectSeats/>}/>
                    </Routes>
                </BrowserRouter>
            </MovieScreeningProvider>
        </div>
    );
}

export default Header;