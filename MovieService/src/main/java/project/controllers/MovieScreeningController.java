package project.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dtos.MovieScreeningDTO;
import project.entities.Movie;
import project.entities.MovieHall;
import project.entities.MovieScreening;
import project.services.MovieHallService;
import project.services.MovieScreeningService;
import project.services.MovieService;

@RestController
@RequestMapping("project/movie_screenings")
@RequiredArgsConstructor
public class MovieScreeningController {
    private final MovieScreeningService movieScreeningService;
    private final MovieService movieService;
    private final MovieHallService movieHallService;
    private final ModelMapper modelMapper;

    @PostMapping("/movie_screening")
    public ResponseEntity<MovieScreening> saveMovieScreening(@RequestBody MovieScreeningDTO movieScreening) {
        Movie movieFound = movieService.findMovieByUuid(movieScreening.getMovie());
        if (movieFound == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        MovieHall movieHallFound = movieHallService.findMovieHallByUuid(movieScreening.getMovieHall());
        if (movieHallFound == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        MovieScreening movieScreeningToSave = new MovieScreening(movieScreening.getId(), movieFound, movieScreening.getDate(), movieScreening.getTime(), movieHallFound, movieScreening.getUuid());
        return new ResponseEntity<>(movieScreeningService.saveMovieScreening(movieScreeningToSave), HttpStatus.CREATED);
    }

    @PutMapping("/movie_screening/{id}")
    public ResponseEntity<MovieScreening> updateMovieScreening(@RequestBody MovieScreeningDTO movieScreeningDTO, @PathVariable int id) {
        MovieScreening movieScreeningToUpdate = movieScreeningService.findMovieScreeningByUuid(id);
        if (movieScreeningToUpdate == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        MovieScreening movieScreeningFinal = modelMapper.map(movieScreeningDTO, MovieScreening.class);
        movieScreeningService.updateMovieScreening(movieScreeningToUpdate, movieScreeningFinal);
        return new ResponseEntity<>(movieScreeningFinal, HttpStatus.OK);
    }
}
