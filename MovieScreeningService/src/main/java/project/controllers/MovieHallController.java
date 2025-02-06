package project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controllers.response.Response;
import project.controllers.response.ResponseType;
import project.dtos.SeatDTO;
import project.entities.MovieHall;
import project.entities.Seat;
import project.services.MovieHallService;
import project.services.SeatService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("project/movie_halls")
@RequiredArgsConstructor
@CrossOrigin
public class MovieHallController {
    private final MovieHallService movieHallService;
    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<Response> getAllMovieHalls() {
        return new ResponseEntity<>(
                Response.builder()
                        .responseType(ResponseType.SUCCESS)
                        .responseObject(movieHallService.getAllMovieHalls())
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("movie_hall/{uuid}/seats")
    public ResponseEntity<Response> getAllSeatsFromMovieHall(@PathVariable int uuid) {
        MovieHall movieHallFound = movieHallService.getMovieHallByUuid(uuid);
        if (movieHallFound == null) {
            return new ResponseEntity<>(
                    Response.builder()
                            .responseType(ResponseType.ERROR)
                            .message("Movie hall with id " + uuid + " does not exist")
                            .build(), HttpStatus.OK);
        }
        List<Seat> seatsFromMovieHall = seatService.getSeatsByMovieHall(movieHallFound);
        List<SeatDTO> seatsFromMovieHallDTOS = new ArrayList<>();
        for(Seat seat: seatsFromMovieHall)
        {
            seatsFromMovieHallDTOS.add(SeatDTO.builder()
                            .seat_number(seat.getSeat_number())
                            .row_number(seat.getRow_number())
                            .available(seat.isAvailable())
                            .uuid(seat.getUuid())
                    .build());
        }
        return new ResponseEntity<>(
                Response.builder()
                        .responseObject(seatsFromMovieHallDTOS)
                        .responseType(ResponseType.SUCCESS)
                        .build(),
                HttpStatus.OK);
    }
}
