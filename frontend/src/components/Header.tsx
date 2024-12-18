import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import {Fragment} from "react";
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import HomePage from "./HomePage.tsx";
import ProgramPage from "./ProgramPage.tsx";
import '../css/Header.css';
import {Redirect, Route} from "react-router";
import {BrowserRouter, Routes} from "react-router-dom";
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
                    </>
                    }/>
                    {/*<Route exact path="/" render={() => <Redirect to="/menu"/>}/>*/}
                </Routes>
            </BrowserRouter>
        </div>
    );
}

// <Fragment>
//     <Navbar>
//         <Navbar.Brand href="#home">Cinema App</Navbar.Brand>
//         <Container className="d-flex justify-content-end sign-in-up-container">
//             <Button className="log-in-button">Log in</Button>
//             <Button className="sign-up-container">Sign up</Button>
//         </Container>
//     </Navbar>
//     <Navbar>
//         <Container className="d-flex justify-content-end navigation-container">
//             <Navbar.Toggle aria-controls="basic-navbar-nav"/>
//             <Navbar.Collapse id="basic-navbar-nav">
//                 <Nav variant="tabs" defaultActiveKey="#home" className="me-auto ml-5 border-0">
//                     <Nav.Link href="#home" className="px-5 border-0">Home</Nav.Link>
//                     <Nav.Link href="#program" className="px-5 border-0">Program</Nav.Link>
//                     <Nav.Link href="#movies" className="px-5 border-0">Movies</Nav.Link>
//                     <Nav.Link href="#label" className="px-5 border-0">Label</Nav.Link>
//                 </Nav>
//             </Navbar.Collapse>
//         </Container>
//     </Navbar>
//
// </Fragment>
// )
// }

export default Header;