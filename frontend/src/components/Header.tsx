import GridColumns from "./GridColumns.tsx";
import ListGroup from "./ListGroup.tsx";
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import {Button} from "react-bootstrap";
function Header() {
    return (
        <Navbar>
            <Navbar.Brand href="#home">Cinema App</Navbar.Brand>
            <Row>
                <Container className="d-flex justify-content-end sign-in-up-container">
                        <Button className="log-in-button">Log in</Button>
                        <Button >Sign up</Button>
                </Container>
                <Container className="d-flex justify-content-end">
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link href="#home">Home</Nav.Link>
                            <Nav.Link href="#about">About</Nav.Link>
                            <Nav.Link href="#services">Services</Nav.Link>
                            <Nav.Link href="#works">Works</Nav.Link>
                            <Nav.Link href="#teams">Teams</Nav.Link>
                            <Nav.Link href="#testimonials">Testimonials</Nav.Link>
                            <Nav.Link href="#pricing">Pricing</Nav.Link>
                            <Nav.Link href="#blog">Blog</Nav.Link>
                            <Nav.Link href="#contact">Contact</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Row>
        </Navbar>
    )
}
export default Header;