import Card from 'react-bootstrap/Card';

function CarouselCard() {
    return (
        <Card style={{width: '18rem'}} className="carousel-card align-center">
            <Card.Img variant="top" src="src/images/PopcornSticker.PNG"/>
            <Card.Body>
                <Card.Text className="carousel-body">
                    Book tickets to movies with just a few clicks! Pick your seats and method of payment and check your
                    email for the confirmation!
                </Card.Text>
            </Card.Body>
        </Card>
    );
}

export default CarouselCard;