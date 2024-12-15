import {Carousel, Image} from "react-bootstrap";
import CarouselCard from "./CarouselCard.tsx";

function HomePage() {
    return (
        <Carousel className="align-content-center" style={{minHeight: '70%'}}>
            <Carousel.Item interval={100000000}>
                <div className="carousel-body">
                    <p style={{minWidth: '30rem'}}>Book tickets to movies
                        with just a few clicks! Pick your seats and method of payment and check your
                        email for the confirmation!</p>
                    <Image  src="src/images/popcorn.png"/>
                </div>
                {/*<CarouselCard></CarouselCard>*/}
            </Carousel.Item>
            <Carousel.Item>
                <CarouselCard></CarouselCard>
            </Carousel.Item>
            <Carousel.Item>
                <CarouselCard></CarouselCard>
            </Carousel.Item>
        </Carousel>
    )
}

export default HomePage;