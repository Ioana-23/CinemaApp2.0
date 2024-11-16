package project.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.entities.MovieHall;
import project.services.MovieHallService;

import java.util.List;

@RestController
@RequestMapping("project/movie_halls")
@RequiredArgsConstructor
public class MovieHallController {
    private final MovieHallService movieHallService;

    @GetMapping
    public ResponseEntity<List<MovieHall>> getAllMovieHalls()
    {
        return new ResponseEntity<>(movieHallService.getAllMovieHalls(), HttpStatus.OK);
    }
}
